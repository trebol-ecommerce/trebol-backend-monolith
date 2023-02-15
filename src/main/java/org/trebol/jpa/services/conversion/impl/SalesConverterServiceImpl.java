/*
 * Copyright (c) 2023 The Trebol eCommerce Project
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

package org.trebol.jpa.services.conversion.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.api.models.*;
import org.trebol.jpa.entities.BillingCompany;
import org.trebol.jpa.entities.Sell;
import org.trebol.jpa.services.conversion.*;

import static org.trebol.config.Constants.BILLING_TYPE_ENTERPRISE;

@Transactional
@Service
public class SalesConverterServiceImpl
  implements SalesConverterService {
  private final BillingCompaniesConverterService billingCompaniesConverterService;
  private final CustomersConverterService customersConverterService;
  private final SalespeopleConverterService salespeopleConverterService;
  private final AddressesConverterService addressesConverterService;
  private final ShippersConverterService shippersConverterService;

  @Autowired
  public SalesConverterServiceImpl(
    BillingCompaniesConverterService billingCompaniesConverterService,
    CustomersConverterService customersConverterService,
    SalespeopleConverterService salespeopleConverterService,
    AddressesConverterService addressesConverterService,
    ShippersConverterService shippersConverterService
  ) {
    this.billingCompaniesConverterService = billingCompaniesConverterService;
    this.customersConverterService = customersConverterService;
    this.salespeopleConverterService = salespeopleConverterService;
    this.addressesConverterService = addressesConverterService;
    this.shippersConverterService = shippersConverterService;
  }

  // TODO this method can be really expensive, please optimize it when the REST API specification includes PATCH and PUT methods
  @Override
  public SellPojo convertToPojo(Sell source) {
    SellPojo target = SellPojo.builder()
      .buyOrder(source.getId())
      .date(source.getDate())
      .netValue(source.getNetValue())
      .taxValue(source.getTaxesValue())
      .totalValue(source.getTotalValue())
      .totalItems(source.getTotalItems())
      .transportValue(source.getTransportValue())
      .token(source.getTransactionToken())
      .build();

    CustomerPojo customer = customersConverterService.convertToPojo(source.getCustomer());
    target.setCustomer(customer);

    target.setStatus(source.getStatus().getName());
    target.setPaymentType(source.getPaymentType().getName());
    target.setBillingType(source.getBillingType().getName());

    if (target.getBillingType().equals(BILLING_TYPE_ENTERPRISE)) {
      BillingCompany sourceBillingCompany = source.getBillingCompany();
      if (sourceBillingCompany != null) {
        BillingCompanyPojo targetBillingCompany = billingCompaniesConverterService.convertToPojo(sourceBillingCompany);
        target.setBillingCompany(targetBillingCompany);
      }
    }

    AddressPojo billingAddress = addressesConverterService.convertToPojo(source.getBillingAddress());
    target.setBillingAddress(billingAddress);

    if (source.getShippingAddress() != null) {
      AddressPojo shippingAddress = addressesConverterService.convertToPojo(source.getShippingAddress());
      target.setShippingAddress(shippingAddress);
    }

    if (source.getShipper() != null) {
      ShipperPojo shipper = shippersConverterService.convertToPojo(source.getShipper());
      target.setShipper(shipper);
    }

    if (source.getSalesperson() != null) {
      SalespersonPojo salesperson = salespeopleConverterService.convertToPojo(source.getSalesperson());
      target.setSalesperson(salesperson);
    }
    return target;
  }

  @Override
  public Sell convertToNewEntity(SellPojo source) {
    Sell target = new Sell();
    // the date can be null even after this method is called, since usually the underlying database can take care of it
    if (source.getDate() != null) {
      target.setDate(source.getDate());
    }

    return target;
  }

  @Override
  public Sell applyChangesToExistingEntity(SellPojo source, Sell target) {
    throw new UnsupportedOperationException("This method is deprecated");
  }
}
