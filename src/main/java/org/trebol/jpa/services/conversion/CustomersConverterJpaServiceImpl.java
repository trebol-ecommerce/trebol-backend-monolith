package org.trebol.jpa.services.conversion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Customer;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.CustomerPojo;
import org.trebol.pojo.PersonPojo;

import javax.annotation.Nullable;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Service
public class CustomersConverterJpaServiceImpl
  implements ITwoWayConverterJpaService<CustomerPojo, Customer> {

  private final ITwoWayConverterJpaService<PersonPojo, Person> peopleService;

  @Autowired
  public CustomersConverterJpaServiceImpl(ITwoWayConverterJpaService<PersonPojo, Person> peopleService) {
    this.peopleService = peopleService;
  }

  @Override
  @Nullable
  public CustomerPojo convertToPojo(Customer source) {
    CustomerPojo target = new CustomerPojo();
    target.setId(source.getId());
    PersonPojo targetPerson = peopleService.convertToPojo(source.getPerson());
    target.setPerson(targetPerson);
    return target;
  }

  @Override
  public Customer convertToNewEntity(CustomerPojo source) throws BadInputException {
    Customer target = new Customer();
    Person targetPerson = peopleService.convertToNewEntity(source.getPerson());
    target.setPerson(targetPerson);
    return target;
  }

  @Override
  public Customer applyChangesToExistingEntity(CustomerPojo source, Customer existing) throws BadInputException {
    Customer target = new Customer(existing);
    Person existingPerson = existing.getPerson();

    PersonPojo sourcePerson = source.getPerson();
    if (sourcePerson == null) {
      throw new BadInputException("Customer must have a person profile");
    }
    Person person = peopleService.applyChangesToExistingEntity(sourcePerson, existingPerson);
    target.setPerson(person);

    return target;
  }
}
