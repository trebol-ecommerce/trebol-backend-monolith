package org.trebol.jpa.services;

import java.text.DateFormat;
import java.text.ParseException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
  private final ConversionService conversion;

  @Autowired
  public SalesJpaCrudServiceImpl(ISalesJpaRepository repository, ConversionService conversion,
    ISellStatusesJpaRepository statusesRepository) {
    super(repository);
    this.salesRepository = repository;
    this.conversion = conversion;
    this.statusesRepository = statusesRepository;
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

  @Override
  public Sell pojo2Entity(SellPojo source) throws BadInputException {
    return conversion.convert(source, Sell.class);
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
        logger.warn("Param '{}' couldn't be parsed as number (value: '{}')", paramName, stringValue, exc);
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
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
}
