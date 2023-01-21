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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.BillingCompany;
import org.trebol.jpa.entities.Sell;
import org.trebol.pojo.*;

@Transactional
@Service
public class SalesConverterJpaServiceImpl
  implements ISalesConverterJpaService {
  private final IBillingCompaniesConverterJpaService billingCompaniesConverter;
  private final ICustomersConverterJpaService customersConverter;
  private final ISalespeopleConverterJpaService salespeopleConverter;
  private final ConversionService conversionService;

  @Autowired
  public SalesConverterJpaServiceImpl(IBillingCompaniesConverterJpaService billingCompaniesConverter,
                                      ICustomersConverterJpaService customersConverter,
                                      ISalespeopleConverterJpaService salespeopleConverter,
                                      ConversionService conversionService) {
    this.billingCompaniesConverter = billingCompaniesConverter;
    this.customersConverter = customersConverter;
    this.salespeopleConverter = salespeopleConverter;
    this.conversionService = conversionService;
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

    target.setStatus(source.getStatus().getName());
    target.setPaymentType(source.getPaymentType().getName());
    target.setBillingType(source.getBillingType().getName());

    if (target.getBillingType().equals("Enterprise Invoice")) {
      BillingCompany sourceBillingCompany = source.getBillingCompany();
      if (sourceBillingCompany != null) {
        BillingCompanyPojo targetBillingCompany = billingCompaniesConverter.convertToPojo(sourceBillingCompany);
        target.setBillingCompany(targetBillingCompany);
      }
    }

    if (source.getBillingAddress() != null) {
      AddressPojo billingAddress = conversionService.convert(source.getBillingAddress(), AddressPojo.class);
      target.setBillingAddress(billingAddress);
    }

    if (source.getShippingAddress() != null) {
      AddressPojo shippingAddress = conversionService.convert(source.getShippingAddress(), AddressPojo.class);
      target.setShippingAddress(shippingAddress);
    }

    CustomerPojo customer = customersConverter.convertToPojo(source.getCustomer());
    target.setCustomer(customer);

    if (source.getSalesperson() != null) {
      SalespersonPojo salesperson = salespeopleConverter.convertToPojo(source.getSalesperson());
      target.setSalesperson(salesperson);
    }
    return target;
  }

  @Transactional
  @Override
  public Sell convertToNewEntity(SellPojo source) throws BadInputException {
    Sell target = new Sell();
    // the date can be null even after this method is called, since usually the underlying database can take care of it
    if (source.getDate() != null) {
      target.setDate(source.getDate());
    }

    return target;
  }

  @Override
  public Sell applyChangesToExistingEntity(SellPojo source, Sell target) throws BadInputException {
    throw new UnsupportedOperationException("This method is deprecated");
  }
}
