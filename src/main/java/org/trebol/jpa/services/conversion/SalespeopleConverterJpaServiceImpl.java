package org.trebol.jpa.services.conversion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.entities.Salesperson;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.PersonPojo;
import org.trebol.pojo.SalespersonPojo;

import javax.annotation.Nullable;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Service
public class SalespeopleConverterJpaServiceImpl
  implements ITwoWayConverterJpaService<SalespersonPojo, Salesperson> {

  private final ITwoWayConverterJpaService<PersonPojo, Person> peopleService;

  @Autowired
  public SalespeopleConverterJpaServiceImpl(ITwoWayConverterJpaService<PersonPojo, Person> peopleService) {
    this.peopleService = peopleService;
  }

  @Override
  @Nullable
  public SalespersonPojo convertToPojo(Salesperson source) {
    SalespersonPojo target = new SalespersonPojo();
    target.setId(source.getId());
    PersonPojo targetPerson = peopleService.convertToPojo(source.getPerson());
    target.setPerson(targetPerson);
    return target;
  }

  @Override
  public Salesperson convertToNewEntity(SalespersonPojo source) throws BadInputException {
    Salesperson target = new Salesperson();
    Person targetPerson = peopleService.convertToNewEntity(source.getPerson());
    target.setPerson(targetPerson);
    return target;
  }

  @Override
  public Salesperson applyChangesToExistingEntity(SalespersonPojo source, Salesperson existing) throws BadInputException {
    Salesperson target = new Salesperson(existing);
    Person existingPerson = existing.getPerson();

    PersonPojo sourcePerson = source.getPerson();
    if (sourcePerson == null) {
      throw new BadInputException("Salesperson must have a person profile");
    }
    Person person = peopleService.applyChangesToExistingEntity(sourcePerson, existingPerson);
    target.setPerson(person);

    return target;
  }
}
