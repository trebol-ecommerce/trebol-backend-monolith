/*
 * Copyright (c) 2022 The Trebol eCommerce Project
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished
 * to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.trebol.jpa.services.conversion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.config.ValidationProperties;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.*;
import org.trebol.jpa.repositories.*;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.*;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.*;
import java.util.regex.Pattern;

// TODO reduce the complexity & code duplication arising from this service
@Transactional
@Service
public class SalesConverterJpaServiceImpl
  implements ITwoWayConverterJpaService<SellPojo, Sell> {

  private static final String IS_NOT_VALID = "is not valid";
  private final ISellStatusesJpaRepository statusesRepository;
  private final IBillingTypesJpaRepository billingTypesRepository;
  private final IPaymentTypesJpaRepository paymentTypesRepository;
  private final IBillingCompaniesJpaRepository billingCompaniesRepository;
  private final IShippersJpaRepository shippersRepository;
  private final IAddressesJpaRepository addressesRepository;
  private final ITwoWayConverterJpaService<BillingCompanyPojo, BillingCompany> billingCompaniesConverter;
  private final ITwoWayConverterJpaService<CustomerPojo, Customer> customersConverter;
  private final GenericCrudJpaService<CustomerPojo, Customer> customersService;
  private final ITwoWayConverterJpaService<SalespersonPojo, Salesperson> salespeopleConverter;
  private final ICustomersJpaRepository customersRepository;
  private final IProductsJpaRepository productsRepository;
  private final ConversionService conversion;
  private final Validator validator;
  private final Pattern companyIdNumberPattern;

  @Autowired
  public SalesConverterJpaServiceImpl(ConversionService conversion,
                                      ISellStatusesJpaRepository statusesRepository,
                                      IBillingTypesJpaRepository billingTypesRepository,
                                      IBillingCompaniesJpaRepository billingCompaniesRepository,
                                      IPaymentTypesJpaRepository paymentTypesRepository,
                                      IAddressesJpaRepository addressesRepository,
                                      IShippersJpaRepository shippersRepository,
                                      ITwoWayConverterJpaService<BillingCompanyPojo, BillingCompany> billingCompaniesConverter,
                                      ITwoWayConverterJpaService<CustomerPojo, Customer> customersConverter,
                                      GenericCrudJpaService<CustomerPojo, Customer> customersService,
                                      ITwoWayConverterJpaService<SalespersonPojo, Salesperson> salespeopleConverter,
                                      ICustomersJpaRepository customersRepository,
                                      IProductsJpaRepository productsRepository,
                                      Validator validator,
                                      ValidationProperties validationProperties) {
    this.conversion = conversion;
    this.statusesRepository = statusesRepository;
    this.billingTypesRepository = billingTypesRepository;
    this.billingCompaniesRepository = billingCompaniesRepository;
    this.paymentTypesRepository = paymentTypesRepository;
    this.addressesRepository = addressesRepository;
    this.shippersRepository = shippersRepository;
    this.billingCompaniesConverter = billingCompaniesConverter;
    this.customersConverter = customersConverter;
    this.customersService = customersService;
    this.salespeopleConverter = salespeopleConverter;
    this.customersRepository = customersRepository;
    this.productsRepository = productsRepository;
    this.validator = validator;
    this.companyIdNumberPattern = Pattern.compile(validationProperties.getIdNumberRegexp());
  }

  @Override
  @Nullable
  public SellPojo convertToPojo(Sell source) {
    // TODO can lesser null checks be used ?
    SellPojo target = conversion.convert(source, SellPojo.class);
    if (target != null) {

      target.setStatus(source.getStatus().getName());
      target.setPaymentType(source.getPaymentType().getName());
      target.setBillingType(source.getBillingType().getName());

      if (target.getBillingType().equals("Enterprise Invoice")) {
        BillingCompany sourceBillingCompany = source.getBillingCompany();
        if (sourceBillingCompany != null) {
          BillingCompanyPojo targetBillingCompany = conversion.convert(sourceBillingCompany, BillingCompanyPojo.class);
          target.setBillingCompany(targetBillingCompany);
        }
      }

      if (source.getBillingAddress() != null) {
        AddressPojo billingAddress = conversion.convert(source.getBillingAddress(), AddressPojo.class);
        target.setBillingAddress(billingAddress);
      }

      if (source.getShippingAddress() != null) {
        AddressPojo shippingAddress = conversion.convert(source.getShippingAddress(), AddressPojo.class);
        target.setShippingAddress(shippingAddress);
      }

      CustomerPojo customer = customersConverter.convertToPojo(source.getCustomer());
      target.setCustomer(customer);

      if (source.getSalesperson() != null) {
        SalespersonPojo salesperson = salespeopleConverter.convertToPojo(source.getSalesperson());
        target.setSalesperson(salesperson);
      }
    }
    return target;
  }

  @Transactional
  @Override
  public Sell convertToNewEntity(SellPojo source) throws BadInputException {
    Sell target = new Sell();

    if (source.getDate() != null) {
      target.setDate(source.getDate());
    }

    this.applyStatus(source, target);
    this.applyPaymentType(source, target);
    this.applyBillingTypeAndCompany(source, target);
    this.applyCustomer(source, target);
    this.applyBillingAddress(source, target);
    this.applyShippingAddress(source, target);
    this.applyShipper(source, target);

    List<SellDetail> entityDetails = this.convertDetails(source.getDetails());
    target.setDetails(entityDetails);

    return target;
  }

  private void applyStatus(SellPojo source, Sell target) throws BadInputException {
    String statusName = source.getStatus();
    if (statusName == null || statusName.isBlank()) {
      statusName = "Pending";
    }

    Optional<SellStatus> existingStatus = statusesRepository.findByName(statusName);
    if (existingStatus.isEmpty()) {
      throw new BadInputException("Status '" + statusName + "' " + IS_NOT_VALID);
    } else {
      target.setStatus(existingStatus.get());
    }
  }

  private void applyPaymentType(SellPojo source, Sell target) throws BadInputException {
    String paymentType = source.getPaymentType();
    if (paymentType == null || paymentType.isBlank()) {
      throw new BadInputException("An accepted payment type is required");
    } else {
      Optional<PaymentType> existingPaymentType = paymentTypesRepository.findByName(paymentType);
      if (existingPaymentType.isEmpty()) {
        throw new BadInputException("Payment type '" + paymentType + "' " + IS_NOT_VALID);
      } else {
        target.setPaymentType(existingPaymentType.get());
      }
    }
  }

  private void applyBillingTypeAndCompany(SellPojo source, Sell target) throws BadInputException {
    String billingType = source.getBillingType();
    if (billingType == null || billingType.isBlank()) {
      billingType = "Bill";
    }

    Optional<BillingType> existingBillingType = billingTypesRepository.findByName(billingType);
    if (existingBillingType.isPresent()) {
      target.setBillingType(existingBillingType.get());
    } else {
      throw new BadInputException("Billing type '" + billingType + "' " + IS_NOT_VALID);
    }

    if (billingType.equals("Enterprise Invoice")) {
      BillingCompanyPojo sourceBillingCompany = source.getBillingCompany();
      if (sourceBillingCompany == null) {
        throw new BadInputException("Billing company details are required to generate enterprise invoices");
      } else {
        BillingCompany billingCompany = fetchOrConvertBillingCompany(sourceBillingCompany);
        target.setBillingCompany(billingCompany);
      }
    }
  }

  private void applyCustomer(SellPojo source, Sell target) throws BadInputException {
    CustomerPojo sourceCustomer = source.getCustomer();
    if (sourceCustomer == null) {
      throw new BadInputException("Customer must possess valid personal information");
    } else {
      Optional<Customer> existing = customersService.getExisting(sourceCustomer);
      if (existing.isPresent()) {
        target.setCustomer(existing.get());
      } else {
        Customer targetCustomer = customersConverter.convertToNewEntity(sourceCustomer);
        targetCustomer = customersRepository.saveAndFlush(targetCustomer);
        target.setCustomer(targetCustomer);
      }
    }
  }

  private void applyBillingAddress(SellPojo source, Sell target) throws BadInputException {
    AddressPojo billingAddress = source.getBillingAddress();
    if (billingAddress != null) {
      try {
        Address targetAddress = this.fetchOrConvertAddress(billingAddress);
        target.setBillingAddress(targetAddress);
      } catch (BadInputException ex) {
        throw new BadInputException("The provided billing address " + IS_NOT_VALID);
      }
    }
  }

  private void applyShippingAddress(SellPojo source, Sell target) throws BadInputException {
    AddressPojo shippingAddress = source.getShippingAddress();
    if (shippingAddress != null) {
      try {
        Address targetAddress = this.fetchOrConvertAddress(shippingAddress);
        target.setShippingAddress(targetAddress);
      } catch (BadInputException ex) {
        throw new BadInputException("The provided shipping address " + IS_NOT_VALID);
      }
    }
  }

  private void applyShipper(SellPojo source, Sell target) throws BadInputException {
    ShipperPojo sourceShipper = source.getShipper();
    if (sourceShipper != null) {
      Set<ConstraintViolation<ShipperPojo>> validations = validator.validate(sourceShipper);
      if (!validations.isEmpty()) {
        throw new BadInputException("Invalid shipper");
      } else {
        Optional<Shipper> byName = shippersRepository.findByName(sourceShipper.getName());
        if (byName.isEmpty()) {
          throw new BadInputException("The specified shipper does not exist");
        } else {
          target.setShipper(byName.get());
        }
      }
    }
  }

  private List<SellDetail> convertDetails(Collection<SellDetailPojo> sourceDetails) throws BadInputException {
    List<SellDetail> details = new ArrayList<>();
    for (SellDetailPojo d : sourceDetails) {
      String barcode = d.getProduct().getBarcode();
      if (barcode == null || barcode.isBlank()) {
        throw new BadInputException("Product barcode must be valid");
      }
      Optional<Product> productByBarcode = productsRepository.findByBarcode(barcode);
      if (productByBarcode.isEmpty()) {
        throw new BadInputException("Unexisting product in sell details");
      }
      Product product = productByBarcode.get();
      SellDetail targetDetail = new SellDetail(d.getUnits(), product);
      targetDetail.setUnitValue(product.getPrice());
      details.add(targetDetail);
    }
    return details;
  }

  private Address fetchOrConvertAddress(AddressPojo source) throws BadInputException {
    Set<ConstraintViolation<AddressPojo>> validations = validator.validate(source);
    if (!validations.isEmpty()) {
      throw new BadInputException("Invalid address");
    } else {
      Optional<Address> matchingAddress = addressesRepository.findByFields(
        source.getCity(),
        source.getMunicipality(),
        source.getFirstLine(),
        source.getSecondLine(),
        source.getPostalCode(),
        source.getNotes());
      return matchingAddress.orElseGet(() -> conversion.convert(source, Address.class));
    }
  }

  private BillingCompany fetchOrConvertBillingCompany(BillingCompanyPojo sourceBillingCompany)
    throws BadInputException {
    String idNumber = sourceBillingCompany.getIdNumber();
    if (idNumber == null || idNumber.isBlank() ) {
      throw new BadInputException("Billing company must have an id number");
    }

    if (this.companyIdNumberPattern.matcher(idNumber).matches()) {
      throw new BadInputException("Billing company must have a correct id number");
    }

    Optional<BillingCompany> matchByIdNumber = billingCompaniesRepository.findByIdNumber(idNumber);
    if (matchByIdNumber.isPresent()) {
      return matchByIdNumber.get();
    } else {
      BillingCompany billingCompany = billingCompaniesConverter.convertToNewEntity(sourceBillingCompany);
      billingCompany = billingCompaniesRepository.saveAndFlush(billingCompany);
      return billingCompany;
    }
  }
}
