package cl.blm.newmarketing.backend.converters.pojo2dto;

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

    if (source.id != null) {
      target.setPersonId(source.id);
    }

    if (source.name != null) {
      target.setPersonFullName(source.name);
    }

    if (source.address != null) {
      target.setPersonAddress(source.address);
    }

    if (source.email != null) {
      target.setPersonEmail(source.email);
    }

    if (source.idCard != null) {
      target.setPersonIdCard(source.idCard);
    }

    if (source.phone1 != null) {
      target.setPersonPhone1(source.phone1);
    }

    if (source.phone2 != null) {
      target.setPersonPhone2(source.phone2);
    }

    return target;
  }
}
