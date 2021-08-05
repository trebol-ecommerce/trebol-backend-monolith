package org.trebol.converters.toentity;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import org.trebol.api.pojo.SalespersonPojo;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.entities.Salesperson;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class Salesperson2Entity
    implements Converter<SalespersonPojo, Salesperson> {

  @Override
  public Salesperson convert(SalespersonPojo source) {
    Salesperson target = new Salesperson();
    target.setId(source.getId());

    Person targetPerson = new Person();
    targetPerson.setId(source.getPerson().getId());
    target.setPerson(targetPerson);

    return target;
  }
}
