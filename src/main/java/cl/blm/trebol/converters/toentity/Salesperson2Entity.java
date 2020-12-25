package cl.blm.trebol.converters.toentity;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.trebol.api.pojo.SalespersonPojo;
import cl.blm.trebol.jpa.entities.Person;
import cl.blm.trebol.jpa.entities.Salesperson;

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
    target.setPerson(new Person(source.getPerson().getId()));
    return target;
  }
}
