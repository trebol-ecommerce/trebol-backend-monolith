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

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.api.models.PersonPojo;
import org.trebol.api.models.UserPojo;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.entities.User;
import org.trebol.jpa.entities.UserRole;
import org.trebol.jpa.repositories.PeopleRepository;
import org.trebol.jpa.repositories.UserRolesRepository;
import org.trebol.jpa.services.patch.UsersPatchService;

import java.util.Optional;

@Transactional
@Service
public class UsersPatchServiceImpl
  implements UsersPatchService {
  private final UserRolesRepository rolesRepository;
  private final PeopleRepository peopleRepository;
  private final PasswordEncoder passwordEncoder;

  public UsersPatchServiceImpl(
    UserRolesRepository rolesRepository,
    PeopleRepository peopleRepository,
    PasswordEncoder passwordEncoder
  ) {
    this.rolesRepository = rolesRepository;
    this.peopleRepository = peopleRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public User patchExistingEntity(UserPojo changes, User existing) {
    User target = new User(existing);

    String name = changes.getName();
    if (name != null && !name.isBlank() && !target.getName().equals(name)) {
      target.setName(name);
    }

    String roleName = changes.getRole();
    if (roleName != null && !roleName.isBlank() && !target.getUserRole().getName().equals(roleName)) {
      Optional<UserRole> roleNameMatch = rolesRepository.findByName(roleName);
      roleNameMatch.ifPresent(target::setUserRole);
    }

    String password = changes.getPassword();
    if (password != null && !password.isBlank() && !passwordEncoder.matches(password, target.getPassword())) {
      String encodedPassword = passwordEncoder.encode(password);
      target.setPassword(encodedPassword);
    }

    PersonPojo person = changes.getPerson();
    if (person != null) {
      String idNumber = person.getIdNumber();
      if (idNumber != null && !idNumber.isBlank() && !target.getPerson().getIdNumber().equals(idNumber)) {
        Optional<Person> idNumberMatch = peopleRepository.findByIdNumber(idNumber);
        idNumberMatch.ifPresent(target::setPerson);
      }
    }

    return target;
  }
}
