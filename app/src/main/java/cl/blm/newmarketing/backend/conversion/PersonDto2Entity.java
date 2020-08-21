package cl.blm.newmarketing.backend.conversion;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.model.entities.Person;
import cl.blm.newmarketing.backend.rest.dtos.PersonDto;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class PersonDto2Entity
    implements Converter<PersonDto, Person> {
  @Override
  public Person convert(PersonDto source) {
    Person target = new Person();
    target.setId(source.getPersonId());
    target.setName(source.getPersonFullName());
    target.setIdCard(source.getPersonIdCard());

    String address = source.getPersonAddress();
    if (address != null && !address.isEmpty()) {
      target.setAddress(address);
    }

    String email = source.getPersonEmail();
    if (email != null && !email.isEmpty()) {
      target.setEmail(email);
    }
    return target;
  }
}
