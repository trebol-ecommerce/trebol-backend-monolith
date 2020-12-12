package cl.blm.trebol.services.exposed.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.querydsl.core.types.Predicate;

import cl.blm.trebol.api.pojo.RegistrationPojo;
import cl.blm.trebol.jpa.entities.QPerson;
import cl.blm.trebol.jpa.entities.QUser;
import cl.blm.trebol.jpa.entities.User;
import cl.blm.trebol.jpa.repositories.PeopleRepository;
import cl.blm.trebol.jpa.repositories.UsersRepository;
import cl.blm.trebol.services.exposed.RegistrationService;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
public class RegistrationServiceImpl
  implements RegistrationService {

  private final PeopleRepository peopleRepository;
  private final UsersRepository usersRepository;

  @Autowired
  public RegistrationServiceImpl(PeopleRepository peopleRepository, UsersRepository usersRepository) {
    this.peopleRepository = peopleRepository;
    this.usersRepository = usersRepository;
  }

  @Override
  public boolean register(RegistrationPojo registration) {
    Predicate userWithSameName = QUser.user.name.eq(registration.getName());
    if (!usersRepository.exists(userWithSameName)) {
      //Predicate sameProfile = QPerson.person.eq(right);
      User newUser = null;
      User savedUser = usersRepository.saveAndFlush(newUser);
    }
    return false;
  }

}
