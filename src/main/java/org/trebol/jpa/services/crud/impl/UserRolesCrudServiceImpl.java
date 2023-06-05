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
import org.trebol.api.models.UserRolePojo;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.jpa.entities.UserRole;
import org.trebol.jpa.repositories.UserRolesRepository;
import org.trebol.jpa.services.conversion.UserRolesConverterService;
import org.trebol.jpa.services.crud.CrudGenericService;
import org.trebol.jpa.services.crud.UserRolesCrudService;
import org.trebol.jpa.services.patch.UserRolesPatchService;

import java.util.Optional;

@Transactional
@Service
public class UserRolesCrudServiceImpl
  extends CrudGenericService<UserRolePojo, UserRole>
  implements UserRolesCrudService {
  private final UserRolesRepository rolesRepository;

  @Autowired
  public UserRolesCrudServiceImpl(
    UserRolesRepository rolesRepository,
    UserRolesConverterService rolesConverterService,
    UserRolesPatchService rolesPatchService
  ) {
    super(rolesRepository, rolesConverterService, rolesPatchService);
    this.rolesRepository = rolesRepository;
  }

  @Override
  public Optional<UserRole> getExisting(UserRolePojo input) throws BadInputException {
    String name = input.getName();
    if (StringUtils.isBlank(name)) {
      throw new BadInputException("Invalid user role name");
    } else {
      return rolesRepository.findByName(name);
    }
  }
}
