package org.trebol.jpa.services.crud;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.User;
import org.trebol.jpa.repositories.IUsersJpaRepository;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.UserPojo;

import java.util.Optional;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Transactional
@Service
public class UsersJpaCrudServiceImpl
  extends GenericCrudJpaService<UserPojo, User> {

  private final IUsersJpaRepository userRepository;

  @Autowired
  public UsersJpaCrudServiceImpl(IUsersJpaRepository repository,
                                 ITwoWayConverterJpaService<UserPojo, User> converter) {
    super(repository,
          converter,
          LoggerFactory.getLogger(UsersJpaCrudServiceImpl.class));
    this.userRepository = repository;
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
}
