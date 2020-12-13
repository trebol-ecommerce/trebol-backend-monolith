package cl.blm.trebol.services.exposed.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
@Service
public class RegistrationServiceImpl
  implements RegistrationService {

  private final PeopleRepository peopleRepository;
  private final UsersRepository usersRepository;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public RegistrationServiceImpl(PeopleRepository peopleRepository, UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
    this.peopleRepository = peopleRepository;
    this.usersRepository = usersRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  @Transactional
  public boolean register(RegistrationPojo registration) {
    Predicate userWithSameName = QUser.user.name.eq(registration.getName());
    if (usersRepository.exists(userWithSameName)) {
      return false;
    }

    Person newPerson = this.createPersonFromRegistrationPojo(registration);
    Predicate sameProfileData = QPerson.person.eq(newPerson);
    if (peopleRepository.exists(sameProfileData)) {
      return false;
    } else {
      newPerson = peopleRepository.saveAndFlush(newPerson);
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
    if (source.getPhone1() != null) {
      target.setPhone1(source.getPhone1());
    }
    if (source.getPhone2() != null) {
      target.setPhone2(source.getPhone2());
    }
    return target;
  }

  protected User createUserFromRegistrationPojo(RegistrationPojo registration) {
    String password = passwordEncoder.encode(registration.getPassword());
    User target = new User();
    target.setName(registration.getName());
    target.setPassword(password);
    return target;
  }

}
