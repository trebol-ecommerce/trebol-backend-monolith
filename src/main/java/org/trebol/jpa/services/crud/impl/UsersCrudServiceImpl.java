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

import com.querydsl.core.types.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.api.models.UserPojo;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.config.SecurityProperties;
import org.trebol.jpa.entities.User;
import org.trebol.jpa.repositories.UsersRepository;
import org.trebol.jpa.services.conversion.UsersConverterService;
import org.trebol.jpa.services.crud.CrudGenericService;
import org.trebol.jpa.services.crud.UsersCrudService;
import org.trebol.jpa.services.patch.UsersPatchService;
import org.trebol.security.exceptions.AccountProtectionViolationException;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Transactional
@Service
public class UsersCrudServiceImpl
  extends CrudGenericService<UserPojo, User> implements UsersCrudService {
  private final UsersRepository usersRepository;
  private final SecurityProperties securityProperties;

  @Autowired
  public UsersCrudServiceImpl(
    UsersRepository usersRepository,
    UsersConverterService usersConverterService,
    UsersPatchService usersPatchService,
    SecurityProperties securityProperties
  ) {
    super(usersRepository, usersConverterService, usersPatchService);
    this.usersRepository = usersRepository;
    this.securityProperties = securityProperties;
  }

  @Override
  public Optional<User> getExisting(UserPojo input) throws BadInputException {
    String name = input.getName();
    if (StringUtils.isBlank(name)) {
      throw new BadInputException("Invalid user name");
    } else {
      return usersRepository.findByName(name);
    }
  }

  @Override
  public void delete(Predicate filters) throws EntityNotFoundException {
    if (securityProperties.isAccountProtectionEnabled()) {
      Optional<User> optionalUser = usersRepository.findOne(filters);
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
