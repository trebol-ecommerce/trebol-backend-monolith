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

package org.trebol.jpa.services.patch.impl;

import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.trebol.api.models.PersonPojo;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.services.patch.PeoplePatchService;

import java.util.Map;

@Service
@NoArgsConstructor
public class PeoplePatchServiceImpl
    implements PeoplePatchService {

    @Override
    public Person patchExistingEntity(Map<String, Object> changes, Person existing) throws BadInputException {
        Person target = new Person(existing);

        if (changes.containsKey("idNumber")) {
            String idNumber = (String) changes.get("idNumber");
            if (!StringUtils.isBlank(idNumber)) {
                target.setIdNumber(idNumber);
            }
        }

        if (changes.containsKey("firstName")) {
            String firstName = (String) changes.get("firstName");
            if (!StringUtils.isBlank(firstName)) {
                target.setFirstName(firstName);
            }
        }

        if (changes.containsKey("lastName")) {
            String lastName = (String) changes.get("lastName");
            if (!StringUtils.isBlank(lastName)) {
                target.setLastName(lastName);
            }
        }

        if (changes.containsKey("email")) {
            String email = (String) changes.get("email");
            if (!StringUtils.isBlank(email)) {
                target.setEmail(email);
            }
        }

        if (changes.containsKey("phone1")) {
            String phone1 = (String) changes.get("phone1");
            target.setPhone1(phone1);
        }

        if (changes.containsKey("phone2")) {
            String phone2 = (String) changes.get("phone2");
            target.setPhone2(phone2);
        }

        return target;
    }

    @Override
    public Person patchExistingEntity(PersonPojo changes, Person existing) throws BadInputException {
        throw new UnsupportedOperationException("This method signature has been deprecated");
    }
}
