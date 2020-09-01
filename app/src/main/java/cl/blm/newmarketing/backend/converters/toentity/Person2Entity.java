package cl.blm.newmarketing.backend.converters.toentity;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.api.pojo.PersonPojo;
import cl.blm.newmarketing.backend.model.entities.Person;

@Component
public class Person2Entity
    implements Converter<PersonPojo, Person> {

  @Override
  public Person convert(PersonPojo source) {
    Person target = new Person(source.id);
    target.setName(source.name);
    target.setIdCard(source.idCard);
    target.setEmail(source.email);
    target.setAddress(source.address);
    if (source.phone1 != null) {
      target.setPhone1(source.phone1);
    }
    if (source.phone2 != null) {
      target.setPhone2(source.phone2);
    }
    return target;
  }
}
