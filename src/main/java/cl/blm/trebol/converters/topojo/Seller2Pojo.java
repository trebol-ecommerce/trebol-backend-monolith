package cl.blm.trebol.converters.topojo;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.trebol.api.pojo.SalespersonPojo;
import cl.blm.trebol.jpa.entities.Salesperson;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class Seller2Pojo
    implements Converter<Salesperson, SalespersonPojo> {

  @Override
  public SalespersonPojo convert(Salesperson source) {
    SalespersonPojo target = new SalespersonPojo();
    target.setId(source.getId());
    return target;
  }
}
