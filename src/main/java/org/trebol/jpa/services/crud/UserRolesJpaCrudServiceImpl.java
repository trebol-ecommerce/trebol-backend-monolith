package org.trebol.jpa.services.crud;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.UserRole;
import org.trebol.jpa.repositories.IUserRolesJpaRepository;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.UserRolePojo;

import java.util.Optional;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Transactional
@Service
public class UserRolesJpaCrudServiceImpl
  extends GenericCrudJpaService<UserRolePojo, UserRole> {

  private final IUserRolesJpaRepository userRolesRepository;

  @Autowired
  public UserRolesJpaCrudServiceImpl(IUserRolesJpaRepository repository,
                                     ITwoWayConverterJpaService<UserRolePojo, UserRole> converter) {
    super(repository,
          converter,
          LoggerFactory.getLogger(UserRolesJpaCrudServiceImpl.class));
    this.userRolesRepository = repository;
  }

  @Override
  public Optional<UserRole> getExisting(UserRolePojo input) throws BadInputException {
    String name = input.getName();
    if (name == null || name.isBlank()) {
      throw new BadInputException("Invalid user role name");
    } else {
      return userRolesRepository.findByName(name);
    }
  }
}
