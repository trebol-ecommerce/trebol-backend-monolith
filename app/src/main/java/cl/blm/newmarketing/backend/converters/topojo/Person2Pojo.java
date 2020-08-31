package cl.blm.newmarketing.backend.converters.topojo;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.api.pojo.PersonPojo;
import cl.blm.newmarketing.backend.model.entities.Person;

@Component
public class Person2Pojo
    implements Converter<Person, PersonPojo> {

  @Override
  public PersonPojo convert(Person source) {
    PersonPojo target = new PersonPojo();
    target.id = source.getId();
    target.idCard = source.getIdCard();
    target.name = source.getName();
    target.email = source.getEmail();
    target.address = source.getAddress();
    target.phone1 = source.getPhone1();
    target.phone2 = source.getPhone2();
    return target;
  }
}
