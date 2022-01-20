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

package org.trebol.jpa.services.crud;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Customer;
import org.trebol.jpa.repositories.ICustomersJpaRepository;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.CustomerPojo;
import org.trebol.pojo.PersonPojo;

import java.util.Optional;

@Transactional
@Service
public class CustomersJpaCrudServiceImpl
  extends GenericCrudJpaService<CustomerPojo, Customer> {

  private final ICustomersJpaRepository customersRepository;

  @Autowired
  public CustomersJpaCrudServiceImpl(ICustomersJpaRepository repository,
                                     ITwoWayConverterJpaService<CustomerPojo, Customer> converter) {
    super(repository,
          converter,
          LoggerFactory.getLogger(CustomersJpaCrudServiceImpl.class));
    this.customersRepository = repository;
  }

  @Override
  public Optional<Customer> getExisting(CustomerPojo input) throws BadInputException {
    PersonPojo person = input.getPerson();
    if (person == null) {
      throw new BadInputException("Customer does not have profile information");
    } else {
      String idNumber = person.getIdNumber();
      if (idNumber == null || idNumber.isBlank()) {
        throw new BadInputException("Customer does not have an ID card");
      } else {
        return customersRepository.findByPersonIdNumber(idNumber);
      }
    }
  }
}
