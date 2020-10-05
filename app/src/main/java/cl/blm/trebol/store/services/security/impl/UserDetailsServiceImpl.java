package cl.blm.trebol.store.services.security.impl;

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

import cl.blm.trebol.store.jpa.entities.Permission;
import cl.blm.trebol.store.jpa.entities.User;
import cl.blm.trebol.store.jpa.repositories.UsersRepository;
import cl.blm.trebol.store.security.pojo.UserDetailsPojo;
import cl.blm.trebol.store.services.security.UserPermissionsService;

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
  private final UsersRepository usersRepository;
  private final UserPermissionsService userPermissionsService;

  @Autowired
  public UserDetailsServiceImpl(
      ConversionService conversionService,
      UsersRepository usersRepository,
      UserPermissionsService userPermissionsService) {
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
      UserDetailsPojo userDetails = new UserDetailsPojo(authorities,
          username,
          user.getPassword(),
          true, true, true, true);
      return userDetails;
    } else {
      throw new UsernameNotFoundException(username);
    }
  }

}
