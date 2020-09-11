package cl.blm.newmarketing.store.services.security.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.querydsl.core.types.Predicate;

import cl.blm.newmarketing.store.jpa.entities.Permission;
import cl.blm.newmarketing.store.jpa.entities.QUserRolePermission;
import cl.blm.newmarketing.store.jpa.entities.User;
import cl.blm.newmarketing.store.jpa.entities.UserRole;
import cl.blm.newmarketing.store.jpa.entities.UserRolePermission;
import cl.blm.newmarketing.store.jpa.repositories.UserRolePermissionsRepository;
import cl.blm.newmarketing.store.jpa.repositories.UsersRepository;

@Service
public class UserDetailsServiceImpl
    implements UserDetailsService {

  @Autowired
  private UsersRepository usersRepository;

  @Autowired
  private UserRolePermissionsRepository userRolePermissionsRepository;

  private Collection<Permission> getAllUserRolePermissions(User source) {
    UserRole sourceUserRole = source.getUserRole();
    Predicate sourceUserRolePermissions = QUserRolePermission.userRolePermission.userRole.eq(sourceUserRole);

    List<Permission> targetList = new ArrayList<>();
    for (UserRolePermission rolePermission : userRolePermissionsRepository.findAll(sourceUserRolePermissions)) {
      Permission p = rolePermission.getPermission();
      targetList.add(p);
    }

    return targetList;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> foundUser = usersRepository.findByName(username);
    if (foundUser.isPresent()) {
      // TODO finish implementation
      User user = foundUser.get();
//      UserDetailsPojo userDetails = new UserDetailsPojo();
      return null;
    } else {
      throw new UsernameNotFoundException(username);
    }
  }

}
