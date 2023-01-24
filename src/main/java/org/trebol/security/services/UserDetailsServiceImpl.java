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

package org.trebol.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.trebol.config.SecurityProperties;
import org.trebol.jpa.entities.Permission;
import org.trebol.jpa.entities.User;
import org.trebol.jpa.entities.UserRolePermission;
import org.trebol.jpa.repositories.UserRolePermissionsJpaRepository;
import org.trebol.jpa.repositories.UsersJpaRepository;
import org.trebol.pojo.UserDetailsPojo;
import org.trebol.security.UserPermissionsService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service required by the DaoAuthenticationProvider bean.
 */
@Service
public class UserDetailsServiceImpl
  implements UserDetailsService {
  private final UsersJpaRepository usersRepository;
  private final UserRolePermissionsJpaRepository rolePermissionsRepository;
  private final UserPermissionsService userPermissionsService;
  private final SecurityProperties securityProperties;

  @Autowired
  public UserDetailsServiceImpl(
    UsersJpaRepository usersRepository,
    UserRolePermissionsJpaRepository rolePermissionsRepository,
    UserPermissionsService userPermissionsService,
    SecurityProperties securityProperties
  ) {
    this.usersRepository = usersRepository;
    this.rolePermissionsRepository = rolePermissionsRepository;
    this.userPermissionsService = userPermissionsService;
    this.securityProperties = securityProperties;
  }

  private List<SimpleGrantedAuthority> convertPermissionList(Iterable<Permission> sourceList) {
    List<SimpleGrantedAuthority> targetList = new ArrayList<>();
    for (Permission source : sourceList) {
      SimpleGrantedAuthority target = new SimpleGrantedAuthority(source.getCode());
      targetList.add(target);
    }
    return targetList;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    if (securityProperties.isGuestUserEnabled() &&
      username.equals(securityProperties.getGuestUserName())) {
      // TODO parameterize role ID
      Iterable<UserRolePermission> rawPermissions = rolePermissionsRepository.deepFindPermissionsByUserRoleId(4L);
      List<Permission> permissions = new ArrayList<>();
      for (UserRolePermission rp : rawPermissions) {
        permissions.add(rp.getPermission());
      }
      List<SimpleGrantedAuthority> authorities = convertPermissionList(permissions);
      return new UserDetailsPojo(authorities, username, "",
        true, true, true, true);
    }
    Optional<User> foundUser = usersRepository.findByNameWithRole(username);
    if (foundUser.isPresent()) {
      User user = foundUser.get();
      Iterable<Permission> permissions = userPermissionsService.loadPermissionsForUser(user);
      List<SimpleGrantedAuthority> authorities = convertPermissionList(permissions);
      return new UserDetailsPojo(authorities, username, user.getPassword(),
        true, true, true, true);
    } else {
      throw new UsernameNotFoundException(username);
    }
  }
}
