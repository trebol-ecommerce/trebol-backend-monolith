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
    Person target = new Person();
    target.setId(source.getId());
    target.setName(source.getName());
    target.setIdNumber(source.getIdNumber());
    target.setEmail(source.getEmail());
    if (source.getPhone1() != null) {
      target.setPhone1(source.getPhone1());
    }
    if (source.getPhone2() != null) {
      target.setPhone2(source.getPhone2());
    }
    return target;
  }
}
