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
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.repositories.IPeopleJpaRepository;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.PersonPojo;

import java.util.Optional;

@Transactional
@Service
public class PeopleJpaCrudServiceImpl
  extends GenericCrudJpaService<PersonPojo, Person> {

  private final IPeopleJpaRepository peopleRepository;

  @Autowired
  public PeopleJpaCrudServiceImpl(IPeopleJpaRepository repository,
                                  ITwoWayConverterJpaService<PersonPojo, Person> converter) {
    super(repository,
          converter,
          LoggerFactory.getLogger(PeopleJpaCrudServiceImpl.class));
    this.peopleRepository = repository;
  }

  @Override
  public Optional<Person> getExisting(PersonPojo input) throws BadInputException {
    String idCard = input.getIdNumber();
    if (idCard == null || idCard.isBlank()) {
      throw new BadInputException("Customer does not have ID card");
    } else {
      return peopleRepository.findByIdNumber(idCard);
    }
  }
}
