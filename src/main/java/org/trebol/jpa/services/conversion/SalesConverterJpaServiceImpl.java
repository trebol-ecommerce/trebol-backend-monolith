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
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.BillingCompany;
import org.trebol.jpa.entities.Customer;
import org.trebol.jpa.entities.Salesperson;
import org.trebol.jpa.entities.Sell;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.*;

@Transactional
@Service
public class SalesConverterJpaServiceImpl
  implements ITwoWayConverterJpaService<SellPojo, Sell> {
  private final ITwoWayConverterJpaService<CustomerPojo, Customer> customersConverter;
  private final ITwoWayConverterJpaService<SalespersonPojo, Salesperson> salespeopleConverter;
  private final ConversionService conversion;

  @Autowired
  public SalesConverterJpaServiceImpl(ConversionService conversion,
                                      ITwoWayConverterJpaService<CustomerPojo, Customer> customersConverter,
                                      ITwoWayConverterJpaService<SalespersonPojo, Salesperson> salespeopleConverter) {
    this.conversion = conversion;
    this.customersConverter = customersConverter;
    this.salespeopleConverter = salespeopleConverter;
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

    return target;
  }
}
