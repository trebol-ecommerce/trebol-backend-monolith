package org.trebol.jpa.services.conversion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.PersonPojo;

import javax.annotation.Nullable;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Service
public class PeopleConverterJpaServiceImpl
  implements ITwoWayConverterJpaService<PersonPojo, Person> {

  private final ConversionService conversion;

  @Autowired
  public PeopleConverterJpaServiceImpl(ConversionService conversion) {
    this.conversion = conversion;
  }

  @Override
  @Nullable
  public PersonPojo convertToPojo(Person source) {
    return conversion.convert(source, PersonPojo.class);
  }

  @Override
  public Person convertToNewEntity(PersonPojo source) {
    return conversion.convert(source, Person.class);
  }

  @Override
  public Person applyChangesToExistingEntity(PersonPojo source, Person existing) throws BadInputException {
    Person target = new Person(existing);

    String firstName = source.getFirstName();
    if (firstName != null && !firstName.isBlank() && !target.getFirstName().equals(firstName)) {
      target.setFirstName(firstName);
    }

    String lastName = source.getLastName();
    if (lastName != null && !lastName.isBlank() && !target.getLastName().equals(lastName)) {
      target.setLastName(lastName);
    }

    String email = source.getEmail();
    if (email != null && !email.isBlank() && !target.getEmail().equals(email)) {
      target.setEmail(email);
    }

    // phones may be empty, but not null
    String phone1 = source.getPhone1();
    if (phone1 != null) {
      if (!target.getPhone1().equals(phone1)) {
        target.setPhone1(phone1);
      }
    }

    String phone2 = source.getPhone2();
    if (phone2 != null) {
      if (!target.getPhone2().equals(phone2)) {
        target.setPhone2(phone2);
      }
    }

    return target;
  }
}
