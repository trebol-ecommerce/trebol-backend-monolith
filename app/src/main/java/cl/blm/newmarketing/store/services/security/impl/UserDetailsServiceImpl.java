package cl.blm.newmarketing.store.services.security.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import cl.blm.newmarketing.store.jpa.entities.Permission;
import cl.blm.newmarketing.store.jpa.entities.User;
import cl.blm.newmarketing.store.jpa.entities.UserRole;
import cl.blm.newmarketing.store.jpa.entities.UserRolePermission;
import cl.blm.newmarketing.store.jpa.repositories.UserRolePermissionsRepository;
import cl.blm.newmarketing.store.jpa.repositories.UsersRepository;
import cl.blm.newmarketing.store.security.pojo.UserDetailsPojo;

@Service
public class UserDetailsServiceImpl
    implements UserDetailsService {

  private final ConversionService conversionService;
  private final UsersRepository usersRepository;
  private final UserRolePermissionsRepository userRolePermissionsRepository;

  @Autowired
  public UserDetailsServiceImpl(
      ConversionService conversionService,
      UsersRepository usersRepository,
      UserRolePermissionsRepository userRolePermissionsRepository) {
    this.conversionService = conversionService;
    this.usersRepository = usersRepository;
    this.userRolePermissionsRepository = userRolePermissionsRepository;
  }

  private Collection<Permission> getAllUserRolePermissions(User source) {
    UserRole sourceUserRole = source.getUserRole();
    Integer userRoleId = sourceUserRole.getId();
    Iterable<UserRolePermission> userRolePermissions = userRolePermissionsRepository
        .deepFindPermissionsByUserRoleId(userRoleId);

    List<Permission> targetList = new ArrayList<>();
    for (UserRolePermission rolePermission : userRolePermissions) {
      Permission p = rolePermission.getPermission();
      targetList.add(p);
    }

    return targetList;
  }

  private List<SimpleGrantedAuthority> convertPermissionList(Collection<Permission> sourceList) {
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
      Collection<Permission> permissions = getAllUserRolePermissions(user);
      List<SimpleGrantedAuthority> authorities = convertPermissionList(permissions);
      UserDetailsPojo userDetails = new UserDetailsPojo(authorities, username, null, true, true, true, true);
      return userDetails;
    } else {
      throw new UsernameNotFoundException(username);
    }
  }

}
