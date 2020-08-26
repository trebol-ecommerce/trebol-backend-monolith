package cl.blm.newmarketing.backend.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.api.pojos.PersonPojo;
import cl.blm.newmarketing.backend.dtos.PersonDto;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class PersonPojo2Dto
    implements Converter<PersonPojo, PersonDto> {

  @Override
  public PersonDto convert(PersonPojo source) {
    PersonDto target = new PersonDto();
    target.setPersonId(source.id);
    target.setPersonFullName(source.name);
    target.setPersonAddress(source.address);
    target.setPersonEmail(source.email);
    target.setPersonIdCard(source.idCard);
    target.setPersonPhone1(source.phone1);
    target.setPersonPhone2(source.phone2);
    return target;
  }
}
