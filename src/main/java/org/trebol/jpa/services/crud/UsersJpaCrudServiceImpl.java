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

import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.config.SecurityProperties;
import org.trebol.exceptions.AccountProtectionViolationException;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.User;
import org.trebol.jpa.repositories.IUsersJpaRepository;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.conversion.IUsersConverterJpaService;
import org.trebol.jpa.services.datatransport.IUsersDataTransportJpaService;
import org.trebol.pojo.UserPojo;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Transactional
@Service
public class UsersJpaCrudServiceImpl
  extends GenericCrudJpaService<UserPojo, User> implements IUsersCrudService {

  private final IUsersJpaRepository userRepository;
  private final SecurityProperties securityProperties;

  @Autowired
  public UsersJpaCrudServiceImpl(IUsersJpaRepository repository,
                                 IUsersConverterJpaService converter,
                                 IUsersDataTransportJpaService dataTransportService,
                                 SecurityProperties securityProperties) {
    super(repository,
          converter,
          dataTransportService);
    this.userRepository = repository;
    this.securityProperties = securityProperties;
  }

  @Override
  public Optional<User> getExisting(UserPojo input) throws BadInputException {
    String name = input.getName();
    if (name == null || name.isBlank()) {
      throw new BadInputException("Invalid user name");
    } else {
      return userRepository.findByName(name);
    }
  }

  @Override
  public void delete(Predicate filters) throws EntityNotFoundException {
    if (securityProperties.isAccountProtectionEnabled()) {
      Optional<User> optionalUser = userRepository.findOne(filters);
      if (optionalUser.isPresent()) {
        User user = optionalUser.get();
        if (user.getId() == securityProperties.getProtectedAccountId()) {
          throw new AccountProtectionViolationException("Protected account cannot be deleted");
        }
      }
    }
    super.delete(filters);
  }

}
