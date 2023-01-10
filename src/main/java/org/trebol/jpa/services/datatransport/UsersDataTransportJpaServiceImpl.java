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

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.entities.User;
import org.trebol.jpa.entities.UserRole;
import org.trebol.jpa.repositories.IPeopleJpaRepository;
import org.trebol.jpa.repositories.IUserRolesJpaRepository;
import org.trebol.pojo.PersonPojo;
import org.trebol.pojo.UserPojo;

import java.util.Optional;

@Transactional
@Service
public class UsersDataTransportJpaServiceImpl
  implements IUsersDataTransportJpaService {
  private final IUserRolesJpaRepository rolesRepository;
  private final IPeopleJpaRepository peopleRepository;
  private final PasswordEncoder passwordEncoder;

  public UsersDataTransportJpaServiceImpl(
    IUserRolesJpaRepository rolesRepository,
    IPeopleJpaRepository peopleRepository,
    PasswordEncoder passwordEncoder
  ) {
    this.rolesRepository = rolesRepository;
    this.peopleRepository = peopleRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public User applyChangesToExistingEntity(UserPojo source, User existing) throws BadInputException {
    User target = new User(existing);

    String name = source.getName();
    if (name != null && !name.isBlank() && !target.getName().equals(name)) {
      target.setName(name);
    }

    String roleName = source.getRole();
    if (roleName != null && !roleName.isBlank() && !target.getUserRole().getName().equals(roleName)) {
      Optional<UserRole> roleNameMatch = rolesRepository.findByName(roleName);
      roleNameMatch.ifPresent(target::setUserRole);
    }

    String password = source.getPassword();
    if (password != null && !password.isBlank() && !passwordEncoder.matches(password, target.getPassword())) {
      String encodedPassword = passwordEncoder.encode(password);
      target.setPassword(encodedPassword);
    }

    PersonPojo person = source.getPerson();
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
