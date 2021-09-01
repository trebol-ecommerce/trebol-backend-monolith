package org.trebol.jpa.services;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nullable;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.api.pojo.AddressPojo;
import org.trebol.api.pojo.BillingCompanyPojo;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import org.trebol.jpa.entities.QSell;

import org.trebol.api.pojo.CustomerPojo;
import org.trebol.api.pojo.PersonPojo;
import org.trebol.api.pojo.ProductPojo;
import org.trebol.api.pojo.SellDetailPojo;
import org.trebol.api.pojo.SellPojo;
import org.trebol.api.pojo.SalespersonPojo;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Customer;
import org.trebol.jpa.entities.Salesperson;
import org.trebol.jpa.entities.Sell;
import org.trebol.jpa.entities.SellDetail;
import org.trebol.jpa.GenericJpaCrudService;
import org.trebol.jpa.entities.SellStatus;

import javassist.NotFoundException;

import org.trebol.jpa.ISalesJpaService;
import org.trebol.jpa.entities.Address;
import org.trebol.jpa.entities.BillingCompany;
import org.trebol.jpa.entities.BillingType;
import org.trebol.jpa.entities.PaymentType;
import org.trebol.jpa.entities.Product;
import org.trebol.jpa.repositories.IAddressesJpaRepository;
import org.trebol.jpa.repositories.IBillingCompaniesJpaRepository;
import org.trebol.jpa.repositories.IBillingTypesJpaRepository;
import org.trebol.jpa.repositories.ICustomersJpaRepository;
import org.trebol.jpa.repositories.IPaymentTypesJpaRepository;
import org.trebol.jpa.repositories.IProductsJpaRepository;
import org.trebol.jpa.repositories.ISalesJpaRepository;
import org.trebol.jpa.repositories.ISellStatusesJpaRepository;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Transactional
@Service
public class SalesJpaCrudServiceImpl
  extends GenericJpaCrudService<SellPojo, Sell>
  implements ISalesJpaService {

  private static final Logger logger = LoggerFactory.getLogger(SalesJpaCrudServiceImpl.class);
  private final ISalesJpaRepository salesRepository;
  private final ISellStatusesJpaRepository statusesRepository;
  private final IBillingTypesJpaRepository billingTypesRepository;
  private final IPaymentTypesJpaRepository paymentTypesRepository;
  private final IBillingCompaniesJpaRepository billingCompaniesRepository;
  private final IAddressesJpaRepository addressesRepository;
  private final ICustomersJpaRepository customersRepository;
  private final IProductsJpaRepository productsRepository;
  private final ConversionService conversion;
  private final Validator validator;

  @Autowired
  public SalesJpaCrudServiceImpl(ISalesJpaRepository repository, ConversionService conversion,
    ISellStatusesJpaRepository statusesRepository, IBillingTypesJpaRepository billingTypesRepository,
    IBillingCompaniesJpaRepository billingCompaniesRepository, IPaymentTypesJpaRepository paymentTypesRepository,
    IAddressesJpaRepository addressesRepository, ICustomersJpaRepository customersRepository,
    IProductsJpaRepository productsRepository,
    Validator validator) {
    super(repository);
    this.salesRepository = repository;
    this.conversion = conversion;
    this.statusesRepository = statusesRepository;
    this.billingTypesRepository = billingTypesRepository;
    this.billingCompaniesRepository = billingCompaniesRepository;
    this.paymentTypesRepository = paymentTypesRepository;
    this.addressesRepository = addressesRepository;
    this.customersRepository = customersRepository;
    this.productsRepository = productsRepository;
    this.validator = validator;
  }

  @Override
  public SellPojo entity2Pojo(Sell source) {
    // TODO can lesser null checks be used ?
    SellPojo target = conversion.convert(source, SellPojo.class);
    if (target != null) {
      if (source.getBillingType() != null) {
        target.setBillingType(source.getBillingType().getName());
      }

      CustomerPojo customer = this.convertCustomerToPojo(source.getCustomer());
      if (customer != null) {
        target.setCustomer(customer);
      }

      SalespersonPojo salesperson = this.convertSalespersonToPojo(source.getSalesperson());
      if (salesperson != null) {
        target.setSalesperson(salesperson);
      }
    }
    return target;
  }

  @Transactional
  @Override
  public Sell pojo2Entity(SellPojo source) throws BadInputException {
    Sell target = new Sell();

    String statusName = source.getStatus();
    if (statusName != null && !statusName.isEmpty()) {
      Optional<SellStatus> existingStatus = statusesRepository.findByName(statusName);
      if (!existingStatus.isPresent()) {
        throw new BadInputException("Status '" + statusName + "' is not valid");
      } else {
        target.setStatus(existingStatus.get());
      }
    }

    String paymentType = source.getPaymentType();
    if (paymentType != null && !paymentType.isBlank()) {
      Optional<PaymentType> existingPaymentType = paymentTypesRepository.findByName(paymentType);
      if (!existingPaymentType.isPresent()) {
        throw new BadInputException("Payment type '" + paymentType + "' is not valid");
      } else {
        target.setPaymentType(existingPaymentType.get());
      }
    }

    String billingType = source.getBillingType();
    if (billingType != null && !billingType.isBlank()) {
      Optional<BillingType> existingBillingType = billingTypesRepository.findByName(billingType);
      if (!existingBillingType.isPresent()) {
        throw new BadInputException("Billing type '" + billingType + "' is not valid");
      } else {
        target.setBillingType(existingBillingType.get());
      }

      if (billingType.equals("Enterprise Invoice")) {
        BillingCompanyPojo sourceBillingCompany = source.getBillingCompany();
        if (sourceBillingCompany == null) {
          throw new BadInputException("A billing company is required");
        } else {
          BillingCompany billingCompany = this.billingCompany2Entity(sourceBillingCompany);
          target.setBillingCompany(billingCompany);
        }
      }
    }

    AddressPojo billingAddress = source.getBillingAddress();
    if (billingAddress != null) {
      Set<ConstraintViolation<AddressPojo>> validationResult = validator.validate(billingAddress);
      if (!validationResult.isEmpty()) {
        throw new BadInputException("The provided billing address is not valid");
      } else {
        Address targetAddress = conversion.convert(billingAddress, Address.class);
        targetAddress = this.mergeAddress(targetAddress);
        target.setBillingAddress(targetAddress);
      }
    }

    AddressPojo shippingAddress = source.getShippingAddress();
    if (shippingAddress != null) {
      Set<ConstraintViolation<AddressPojo>> validationResult = validator.validate(shippingAddress);
      if (!validationResult.isEmpty()) {
        throw new BadInputException("The provided shipping address is not valid");
      } else {
        Address targetAddress = conversion.convert(shippingAddress, Address.class);
        targetAddress = this.mergeAddress(targetAddress);
        target.setShippingAddress(targetAddress);
      }
    }

    CustomerPojo sourceCustomer = source.getCustomer();
    if (sourceCustomer != null && sourceCustomer.getPerson() != null) {
      Customer customer = this.customer2Entity(sourceCustomer);
      target.setCustomer(customer);
    }

    Collection<SellDetailPojo> sourceDetails = source.getDetails();
    if (sourceDetails != null && !sourceDetails.isEmpty()) {
      List<SellDetail> details = this.details2EntityList(sourceDetails);
      target.setDetails(details);
    }

    return target;
  }

  @Override
  public Page<Sell> getAllEntities(Pageable paged, Predicate filters) {
    if (filters == null) {
      return salesRepository.deepFindAll(paged);
    } else {
      return salesRepository.findAll(filters, paged);
    }
  }

  @Override
  public Predicate parsePredicate(Map<String, String> queryParamsMap) {
    QSell qSell = QSell.sell;
    BooleanBuilder predicate = new BooleanBuilder();
    for (String paramName : queryParamsMap.keySet()) {
      String stringValue = queryParamsMap.get(paramName);
      try {
        Long longValue = Long.valueOf(stringValue);
        Instant valueAsInstant = Instant.parse(stringValue);
        switch (paramName) {
          case "id":
            return predicate.and(qSell.id.eq(longValue)); // match por id es único
          case "date":
            predicate.and(qSell.date.eq(valueAsInstant));
            break;
          // TODO add more filters
          default:
            break;
        }
      } catch (NumberFormatException exc) {
        logger.info("Param '{}' couldn't be parsed as number (value: '{}')", paramName, stringValue);
      }
    }

    return predicate;
  }

  @Nullable
  @Override
  public SellPojo readOne(Long id) {
    Optional<Sell> personById = salesRepository.deepFindById(id);
    if (!personById.isPresent()) {
      return null;
    } else {
      Sell found = personById.get();
      SellPojo foundPojo = this.entity2Pojo(found);
      if (foundPojo != null) {
        List<SellDetailPojo> sellDetails = this.convertDetailsToPojo(found.getDetails());
        foundPojo.setDetails(sellDetails);
      }
      return foundPojo;
    }
  }

  @Override
  public SellPojo find(Predicate conditions) throws NotFoundException {
    Optional<Sell> matchingSell = salesRepository.findOne(conditions);
    if (!matchingSell.isPresent()) {
      throw new NotFoundException("The requested item does not exist");
    } else {
      Sell found = matchingSell.get();
      SellPojo foundPojo = this.entity2Pojo(found);

      // TODO implement this
      //List<SellDetailPojo> pojoDetails = this.details2PojoList(found);
      //foundPojo.setDetails(pojoDetails);
      return foundPojo;
    }
  }

  @Override
  public void setSellStatusToPaymentStartedWithToken(Long id, String token) throws NotFoundException {
    this.setSellStatusByName(id, "Payment Started");
    salesRepository.setTransactionToken(id, token);
  }

  @Override
  public void setSellStatusToPaymentAborted(Long id) throws NotFoundException {
    this.setSellStatusByName(id, "Payment Cancelled");
  }

  @Override
  public void setSellStatusToPaymentFailed(Long id) throws NotFoundException {
    this.setSellStatusByName(id, "Payment Failed");
  }

  @Override
  public void setSellStatusToPaidUnconfirmed(Long id) throws NotFoundException {
    this.setSellStatusByName(id, "Paid, Unconfirmed");
  }

  @Override
  public void setSellStatusToPaidConfirmed(Long id) throws NotFoundException {
    this.setSellStatusByName(id, "Paid, Confirmed");
  }

  @Override
  public void setSellStatusToRejected(Long id) throws NotFoundException {
    this.setSellStatusByName(id, "Rejected");
  }

  @Override
  public void setSellStatusToCompleted(Long id) throws NotFoundException {
    this.setSellStatusByName(id, "Delivery Complete");
  }

  @Transactional
  private void setSellStatusByName(Long sellId, String statusName) throws NotFoundException {
    if (!salesRepository.existsById(sellId)) {
      throw new NotFoundException("The specified sell does not exist");
    } else {
      Optional<SellStatus> statusEntityByName = statusesRepository.findByName(statusName);
      if (statusEntityByName.isPresent()) {
        SellStatus statusEntity = statusEntityByName.get();
        Integer statusChangeResponse = salesRepository.setStatus(sellId, statusEntity);
        logger.debug("statusChangeResponse={}", statusChangeResponse);
      } else {
        logger.error("No sell status exists by the name '{}'", statusName);
      }
    }
  }

  @Nullable
  private CustomerPojo convertCustomerToPojo(Customer source) {
    CustomerPojo customer = conversion.convert(source, CustomerPojo.class);
    if (customer != null) {
      PersonPojo person = conversion.convert(source.getPerson(), PersonPojo.class);
      if (person != null) {
        customer.setPerson(person);
      }
    }
    return customer;
  }

  private List<SellDetailPojo> convertDetailsToPojo(Collection<SellDetail> source) {
    List<SellDetailPojo> sellDetails = new ArrayList<>();
    for (SellDetail sourceSellDetail : source) {
      SellDetailPojo targetSellDetail = conversion.convert(sourceSellDetail, SellDetailPojo.class);
      if (targetSellDetail != null) {
        ProductPojo product = conversion.convert(sourceSellDetail.getProduct(), ProductPojo.class);
        if (product != null) {
          targetSellDetail.setProduct(product);
        }
        sellDetails.add(targetSellDetail);
      }
    }
    return sellDetails;
  }

  @Nullable
  private SalespersonPojo convertSalespersonToPojo(Salesperson source) {
    SalespersonPojo target = conversion.convert(source, SalespersonPojo.class);
    if (target != null) {
      PersonPojo person = conversion.convert(source.getPerson(), PersonPojo.class);
      if (person != null) {
        target.setPerson(person);
      }
    }
    return target;
  }

  @Override
  public boolean itemExists(SellPojo input) throws BadInputException {
    Long id = input.getId();
    return ((id != null) && this.salesRepository.findById(id).isPresent());
  }

  private BillingCompany billingCompany2Entity(BillingCompanyPojo source) throws BadInputException {
    BillingCompany billingCompany;
    String idNumber = source.getIdNumber();
    // TODO parameterize regex/make a bean for pattern
    Pattern rutPattern = Pattern.compile("^\\d{7,9}[\\dk]$");
    Matcher rutMatcher = rutPattern.matcher(idNumber);
    if (idNumber == null || idNumber.isBlank() || !rutMatcher.matches()) {
      throw new BadInputException("Billing company's id number is not a valid RUT");
    } else {
      Optional<BillingCompany> companyByIdNumber = billingCompaniesRepository.findByIdNumber(idNumber);
      if (companyByIdNumber.isPresent()) {
        billingCompany = companyByIdNumber.get();
      } else {
        billingCompany = conversion.convert(source, BillingCompany.class);
      }
      return billingCompany;
    }
  }

  private Address mergeAddress(Address source) {
    Optional<Address> matchingAddress = addressesRepository.findByFields(
        source.getCity(),
        source.getMunicipality(),
        source.getFirstLine(),
        source.getSecondLine(),
        source.getPostalCode(),
        source.getNotes());
    if (matchingAddress.isPresent()) {
      return matchingAddress.get();
    } else {
      Address target = addressesRepository.saveAndFlush(source);
      return target;
    }
  }

  private Customer customer2Entity(CustomerPojo sourceCustomer) {
    Optional<Customer> customerByIdCard = customersRepository.findByPersonIdNumber(sourceCustomer.getPerson().getIdNumber());
    if (customerByIdCard.isPresent()) {
      Customer target = customerByIdCard.get();
      return target;
    } else {
      return conversion.convert(sourceCustomer, Customer.class);
    }
  }

  private List<SellDetail> details2EntityList(Collection<SellDetailPojo> source) throws BadInputException {
    List<SellDetail> details = new ArrayList<>();
    for (SellDetailPojo d : source) {
      try {
        SellDetail targetDetail = this.sellDetail2Entity(d);
        details.add(targetDetail);
      } catch (NotFoundException exc) {
        throw new BadInputException("Unexisting product in sell details");
      }
    }
    return details;
  }

  private SellDetail sellDetail2Entity(SellDetailPojo d) throws NotFoundException {
    String barcode = d.getProduct().getBarcode();
    Optional<Product> productByBarcode = productsRepository.findByBarcode(barcode);
    if (!productByBarcode.isPresent()) {
      throw new NotFoundException("There is no product with barcode '" + barcode + "'");
    } else {
      Product targetProduct = productByBarcode.get();
      SellDetail targetDetail = new SellDetail();
      targetDetail.setProduct(targetProduct);
      targetDetail.setUnits(d.getUnits());
      return targetDetail;
    }
  }
}
