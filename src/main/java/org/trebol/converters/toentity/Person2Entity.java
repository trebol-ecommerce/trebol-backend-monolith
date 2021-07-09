package org.trebol.converters.toentity;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import org.trebol.api.pojo.PersonPojo;
import org.trebol.jpa.entities.Person;

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
    if (source.getAddress() != null) {
      target.setAddress(source.getAddress());
    }
    if (source.getPhone1() != null) {
      target.setPhone1(source.getPhone1());
    }
    if (source.getPhone2() != null) {
      target.setPhone2(source.getPhone2());
    }
    return target;
  }
}
