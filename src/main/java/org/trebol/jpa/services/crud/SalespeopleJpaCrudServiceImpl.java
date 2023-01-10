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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Salesperson;
import org.trebol.jpa.repositories.ISalespeopleJpaRepository;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.conversion.ISalespeopleConverterJpaService;
import org.trebol.jpa.services.datatransport.ISalespeopleDataTransportJpaService;
import org.trebol.pojo.PersonPojo;
import org.trebol.pojo.SalespersonPojo;

import java.util.Optional;

@Transactional
@Service
public class SalespeopleJpaCrudServiceImpl
  extends GenericCrudJpaService<SalespersonPojo, Salesperson>
  implements ISalespeopleCrudService {

  private final ISalespeopleJpaRepository salespeopleRepository;

  @Autowired
  public SalespeopleJpaCrudServiceImpl(ISalespeopleJpaRepository repository,
                                       ISalespeopleConverterJpaService converter,
                                       ISalespeopleDataTransportJpaService dataTransportService) {
    super(repository,
          converter,
          dataTransportService);
    this.salespeopleRepository = repository;
  }

  @Override
  public Optional<Salesperson> getExisting(SalespersonPojo input) throws BadInputException {
    PersonPojo person = input.getPerson();
    if (person == null) {
      throw new BadInputException("Salesperson does not have profile information");
    } else {
      String idNumber = person.getIdNumber();
      if (idNumber == null) {
        throw new BadInputException("Salesperson does not have an ID card");
      } else {
        return salespeopleRepository.findByPersonIdNumber(idNumber);
      }
    }
  }
}
