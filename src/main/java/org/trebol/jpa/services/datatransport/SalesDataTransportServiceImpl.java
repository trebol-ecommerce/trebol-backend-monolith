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

package org.trebol.jpa.services.datatransport;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.*;
import org.trebol.jpa.repositories.*;
import org.trebol.jpa.services.conversion.AddressesConverterService;
import org.trebol.jpa.services.conversion.BillingCompaniesConverterService;
import org.trebol.jpa.services.conversion.CustomersConverterService;
import org.trebol.jpa.services.crud.CustomersCrudService;
import org.trebol.jpa.services.helpers.RegexMatcherAdapterService;
import org.trebol.pojo.*;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Optional;
import java.util.Set;

import static org.trebol.config.Constants.BILLING_TYPE_ENTERPRISE;
import static org.trebol.config.Constants.BILLING_TYPE_INDIVIDUAL;

@Transactional
@Service
public class SalesDataTransportServiceImpl
  implements SalesDataTransportService {
  private static final String IS_NOT_VALID = "is not valid";
  private final SellStatusesRepository statusesRepository;
  private final BillingTypesRepository billingTypesRepository;
  private final PaymentTypesRepository paymentTypesRepository;
  private final BillingCompaniesRepository billingCompaniesRepository;
  private final ShippersRepository shippersRepository;
  private final AddressesRepository addressesRepository;
  private final BillingCompaniesConverterService billingCompaniesConverter;
  private final CustomersConverterService customersConverter;
  private final CustomersCrudService customersService;
  private final CustomersRepository customersRepository;
  private final AddressesConverterService addressesConverterService;
  private final Validator validator;
  private final RegexMatcherAdapterService regexMatcherAdapterService;

  @Autowired
  public SalesDataTransportServiceImpl(
    AddressesConverterService addressesConverterService,
    SellStatusesRepository statusesRepository,
    BillingTypesRepository billingTypesRepository,
    BillingCompaniesRepository billingCompaniesRepository,
    PaymentTypesRepository paymentTypesRepository,
    AddressesRepository addressesRepository,
    ShippersRepository shippersRepository,
    BillingCompaniesConverterService billingCompaniesConverter,
    CustomersConverterService customersConverter,
    CustomersCrudService customersService,
    CustomersRepository customersRepository,
    Validator validator,
    RegexMatcherAdapterService regexMatcherAdapterService
  ) {
    this.addressesConverterService = addressesConverterService;
    this.statusesRepository = statusesRepository;
    this.billingTypesRepository = billingTypesRepository;
    this.billingCompaniesRepository = billingCompaniesRepository;
    this.paymentTypesRepository = paymentTypesRepository;
    this.addressesRepository = addressesRepository;
    this.shippersRepository = shippersRepository;
    this.billingCompaniesConverter = billingCompaniesConverter;
    this.customersConverter = customersConverter;
    this.customersService = customersService;
    this.customersRepository = customersRepository;
    this.validator = validator;
    this.regexMatcherAdapterService = regexMatcherAdapterService;
  }

  @Transactional
  @Override
  public Sell applyChangesToExistingEntity(SellPojo source, Sell existing) throws BadInputException {
    Sell target = new Sell(existing);

    if (source.getDate() != null) {
      target.setDate(source.getDate());
    }
    if (source.getStatus() != null) {
      this.applyStatus(source, target);
    }
    if (source.getPaymentType() != null) {
      this.applyPaymentType(source, target);
    }
    if (source.getBillingType() != null) {
      this.applyBillingTypeAndCompany(source, target);
    }
    if (source.getCustomer() != null && source.getCustomer().getPerson() != null) {
      this.applyCustomer(source, target);
    }
    if (source.getBillingAddress() != null) {
      this.applyBillingAddress(source, target);
    }
    if (source.getShippingAddress() != null) {
      this.applyShippingAddress(source, target);
    }

    if (source.getShipper() != null) {
      this.applyShipper(source, target);
    }

    return target;
  }

  private void applyStatus(SellPojo source, Sell target) throws BadInputException {
    String statusName = source.getStatus();
    if (StringUtils.isBlank(statusName)) {
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
    if (StringUtils.isBlank(paymentType)) {
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
    if (StringUtils.isBlank(billingType)) {
      billingType = BILLING_TYPE_INDIVIDUAL;
    }

    Optional<BillingType> existingBillingType = billingTypesRepository.findByName(billingType);
    if (existingBillingType.isPresent()) {
      target.setBillingType(existingBillingType.get());
    } else {
      throw new BadInputException("Billing type '" + billingType + "' " + IS_NOT_VALID);
    }

    if (billingType.equals(BILLING_TYPE_ENTERPRISE)) {
      BillingCompanyPojo sourceBillingCompany = source.getBillingCompany();
      if (sourceBillingCompany == null) {
        throw new BadInputException("Billing company details are required to generate enterprise invoices");
      } else {
        BillingCompany billingCompany = this.fetchOrConvertBillingCompany(sourceBillingCompany);
        target.setBillingCompany(billingCompany);
      }
    }
  }

  private void applyCustomer(SellPojo source, Sell target) throws BadInputException {
    CustomerPojo sourceCustomer = source.getCustomer();
    if (StringUtils.isBlank(sourceCustomer.getPerson().getIdNumber())) {
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
      if (matchingAddress.isEmpty()) {
        return addressesConverterService.convertToNewEntity(source);
      }
      return matchingAddress.get();
    }
  }

  private BillingCompany fetchOrConvertBillingCompany(BillingCompanyPojo sourceBillingCompany)
    throws BadInputException {
    String idNumber = sourceBillingCompany.getIdNumber();
    if (StringUtils.isBlank(idNumber)) {
      throw new BadInputException("Billing company must have an id number");
    }

    if (!regexMatcherAdapterService.isAValidIdNumber(idNumber)) {
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
