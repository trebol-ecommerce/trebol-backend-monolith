package org.trebol.security.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import org.trebol.jpa.entities.Permission;
import org.trebol.jpa.entities.User;
import org.trebol.pojo.UserDetailsPojo;
import org.trebol.jpa.repositories.IUsersJpaRepository;
import org.trebol.security.IUserPermissionsService;

/**
 * Service required by the DaoAuthenticationProvider bean.
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 *
 */
@Service
public class UserDetailsServiceImpl
    implements UserDetailsService {

  private final ConversionService conversionService;
  private final IUsersJpaRepository usersRepository;
  private final IUserPermissionsService userPermissionsService;

  @Autowired
  public UserDetailsServiceImpl(
      ConversionService conversionService,
      IUsersJpaRepository usersRepository,
      IUserPermissionsService userPermissionsService) {
    this.conversionService = conversionService;
    this.usersRepository = usersRepository;
    this.userPermissionsService = userPermissionsService;
  }

  private List<SimpleGrantedAuthority> convertPermissionList(Iterable<Permission> sourceList) {
    List<SimpleGrantedAuthority> targetList = new ArrayList<>();
    for (Permission source : sourceList) {
      SimpleGrantedAuthority target = conversionService.convert(source, SimpleGrantedAuthority.class);
      targetList.add(target);
    }
    return targetList;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
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
