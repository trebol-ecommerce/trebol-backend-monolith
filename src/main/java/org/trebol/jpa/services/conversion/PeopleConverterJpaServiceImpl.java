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
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.PersonPojo;

@Service
public class PeopleConverterJpaServiceImpl
  implements ITwoWayConverterJpaService<PersonPojo, Person> {

  private final ConversionService conversion;

  @Autowired
  public PeopleConverterJpaServiceImpl(ConversionService conversion) {
    this.conversion = conversion;
  }

  @Override
  @Nullable
  public PersonPojo convertToPojo(Person source) {
    return conversion.convert(source, PersonPojo.class);
  }

  @Override
  public Person convertToNewEntity(PersonPojo source) {
    return conversion.convert(source, Person.class);
  }

  @Override
  public Person applyChangesToExistingEntity(PersonPojo source, Person existing) throws BadInputException {
    Person target = new Person(existing);

    String firstName = source.getFirstName();
    if (firstName != null && !firstName.isBlank() && !target.getFirstName().equals(firstName)) {
      target.setFirstName(firstName);
    }

    String lastName = source.getLastName();
    if (lastName != null && !lastName.isBlank() && !target.getLastName().equals(lastName)) {
      target.setLastName(lastName);
    }

    String email = source.getEmail();
    if (email != null && !email.isBlank() && !target.getEmail().equals(email)) {
      target.setEmail(email);
    }

    // phones may be empty, but not null
    String phone1 = source.getPhone1();

      if (phone1 != null && !target.getPhone1().equals(phone1)) {
        target.setPhone1(phone1);
      }


    String phone2 = source.getPhone2();
    if (phone2 != null) {
      if (!target.getPhone2().equals(phone2)) {
        target.setPhone2(phone2);
      }
    }

    return target;
  }
}
