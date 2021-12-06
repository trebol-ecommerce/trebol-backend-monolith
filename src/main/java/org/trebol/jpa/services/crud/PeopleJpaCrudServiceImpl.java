package org.trebol.jpa.services.crud;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.repositories.IPeopleJpaRepository;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.PersonPojo;

import java.util.Optional;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Transactional
@Service
public class PeopleJpaCrudServiceImpl
  extends GenericCrudJpaService<PersonPojo, Person> {

  private final IPeopleJpaRepository peopleRepository;

  @Autowired
  public PeopleJpaCrudServiceImpl(IPeopleJpaRepository repository,
                                  ITwoWayConverterJpaService<PersonPojo, Person> converter) {
    super(repository,
          converter,
          LoggerFactory.getLogger(PeopleJpaCrudServiceImpl.class));
    this.peopleRepository = repository;
  }

  @Override
  public Optional<Person> getExisting(PersonPojo input) throws BadInputException {
    String idCard = input.getIdNumber();
    if (idCard == null || idCard.isBlank()) {
      throw new BadInputException("Customer does not have ID card");
    } else {
      return peopleRepository.findByIdNumber(idCard);
    }
  }
}
