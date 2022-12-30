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
import org.springframework.stereotype.Service;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Person;
import org.trebol.pojo.PersonPojo;

@Service
public class PeopleDataTransportJpaServiceImpl
  implements IPeopleDataTransportJpaService {

  public PeopleDataTransportJpaServiceImpl(
  ) {
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
    if (!StringUtils.equals(target.getPhone1(),phone1)) {
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
