package cl.blm.newmarketing.backend.conversion;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.pojos.PersonPojo;
import cl.blm.newmarketing.backend.rest.dtos.PersonDto;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class PersonDto2Pojo
    implements Converter<PersonDto, PersonPojo> {
  @Override
  public PersonPojo convert(PersonDto source) {
    PersonPojo target = new PersonPojo();
    target.id = source.getPersonId();
    target.name = source.getPersonFullName();
    target.idCard = source.getPersonIdCard();

    String address = source.getPersonAddress();
    if (address != null && !address.isEmpty()) {
      target.address = address;
    }

    String email = source.getPersonEmail();
    if (email != null && !email.isEmpty()) {
      target.email = email;
    }
    return target;
  }
}
