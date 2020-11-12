package cl.blm.trebol.converters.topojo;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.trebol.api.pojo.PersonPojo;
import cl.blm.trebol.jpa.entities.Person;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class Person2Pojo
    implements Converter<Person, PersonPojo> {

  @Override
  public PersonPojo convert(Person source) {
    PersonPojo target = new PersonPojo();
    target.setId(source.getId());
    target.setIdCard(source.getIdCard());
    target.setName(source.getName());
    target.setEmail(source.getEmail());
    target.setAddress(source.getAddress());
    target.setPhone1(source.getPhone1());
    target.setPhone2(source.getPhone2());
    return target;
  }
}
