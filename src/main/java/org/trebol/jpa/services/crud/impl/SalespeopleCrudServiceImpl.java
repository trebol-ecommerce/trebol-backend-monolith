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

package org.trebol.jpa.services.crud.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.api.models.SalespersonPojo;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.jpa.entities.Salesperson;
import org.trebol.jpa.repositories.SalespeopleRepository;
import org.trebol.jpa.services.conversion.SalespeopleConverterService;
import org.trebol.jpa.services.crud.CrudGenericService;
import org.trebol.jpa.services.crud.SalespeopleCrudService;
import org.trebol.jpa.services.patch.SalespeoplePatchService;

import java.util.Optional;

@Transactional
@Service
public class SalespeopleCrudServiceImpl
  extends CrudGenericService<SalespersonPojo, Salesperson>
  implements SalespeopleCrudService {
  private final SalespeopleRepository salespeopleRepository;

  @Autowired
  public SalespeopleCrudServiceImpl(
    SalespeopleRepository salespeopleRepository,
    SalespeopleConverterService salespeopleConverterService,
    SalespeoplePatchService salespeoplePatchService
  ) {
    super(salespeopleRepository, salespeopleConverterService, salespeoplePatchService);
    this.salespeopleRepository = salespeopleRepository;
  }

  @Override
  public Optional<Salesperson> getExisting(SalespersonPojo input) throws BadInputException {
    String idNumber = input.getPerson().getIdNumber();
    if (StringUtils.isBlank(idNumber)) {
      throw new BadInputException("Salesperson does not have an ID card");
    } else {
      return salespeopleRepository.findByPersonIdNumber(idNumber);
    }
  }
}
