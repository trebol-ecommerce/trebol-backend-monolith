package cl.blm.trebol.services.exposed.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.querydsl.core.types.Predicate;

import cl.blm.trebol.api.pojo.RegistrationPojo;
import cl.blm.trebol.jpa.entities.Person;
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
    if (usersRepository.exists(userWithSameName)) {
      return false;
    }

    Person newPerson = this.createPersonFromRegistrationPojo(registration);
    Predicate sameProfileData = QPerson.person.eq(newPerson);
    if (peopleRepository.exists(sameProfileData)) {
      return false;
    }

    User newUser = this.createUserFromRegistrationPojo(registration);
    newUser.setPerson(newPerson);
    User savedUser = usersRepository.saveAndFlush(newUser);

    return true;
  }

  protected Person createPersonFromRegistrationPojo(RegistrationPojo registration) {
    RegistrationPojo.Profile source = registration.getProfile();
    Person target = new Person();
    target.setName(source.getName());
    target.setIdCard(source.getIdCard());
    target.setEmail(source.getEmail());
    target.setAddress(source.getAddress());
    target.setPhone1(source.getPhone1());
    target.setPhone2(source.getPhone2());
    return target;
  }

  protected User createUserFromRegistrationPojo(RegistrationPojo registration) {
    User target = new User();
    target.setName(registration.getName());
//    target.setPassword(registration.getName());
    return target;
  }

}
