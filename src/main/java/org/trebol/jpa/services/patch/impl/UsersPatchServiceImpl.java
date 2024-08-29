/*
 * Copyright (c) 2020-2024 The Trebol eCommerce Project
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

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.api.models.UserPojo;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.jpa.entities.User;
import org.trebol.jpa.repositories.UserRolesRepository;
import org.trebol.jpa.services.patch.UsersPatchService;

import java.util.Map;

@Transactional
@Service
public class UsersPatchServiceImpl
    implements UsersPatchService {
    private final UserRolesRepository rolesRepository;

    public UsersPatchServiceImpl(
        UserRolesRepository rolesRepository
    ) {
        this.rolesRepository = rolesRepository;
    }

    @Override
    public User patchExistingEntity(Map<String, Object> changes, User existing) throws BadInputException {
        User target = new User(existing);

        if (changes.containsKey("name")) {
            String name = (String) changes.get("name");
            if (!StringUtils.isBlank(name)) {
                target.setName(name);
            }
        }

        if (changes.containsKey("role")) {
            String roleName = (String) changes.get("role");
            if (!StringUtils.isBlank(roleName)) {
                rolesRepository.findByName(roleName).ifPresent(target::setUserRole);
            }
        }

        return target;
    }

    @Override
    public User patchExistingEntity(UserPojo changes, User existing) throws BadInputException {
        throw new UnsupportedOperationException("This method signature has been deprecated");
    }
}
