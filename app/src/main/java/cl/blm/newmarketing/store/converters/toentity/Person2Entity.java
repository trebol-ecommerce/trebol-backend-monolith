package cl.blm.newmarketing.store.converters.toentity;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.store.api.pojo.PersonPojo;
import cl.blm.newmarketing.store.jpa.entities.Person;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class Person2Entity
    implements Converter<PersonPojo, Person> {

  @Override
  public Person convert(PersonPojo source) {
    Person target = new Person(source.getId());
    target.setName(source.getName());
    target.setIdCard(source.getIdCard());
    target.setEmail(source.getEmail());
    target.setAddress(source.getAddress());
    if (source.getPhone1() != null) {
      target.setPhone1(source.getPhone1());
    }
    if (source.getPhone2() != null) {
      target.setPhone2(source.getPhone2());
    }
    return target;
  }
}
