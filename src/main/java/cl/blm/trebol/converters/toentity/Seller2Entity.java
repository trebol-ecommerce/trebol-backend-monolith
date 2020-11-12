package cl.blm.trebol.converters.toentity;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.trebol.api.pojo.SellerPojo;
import cl.blm.trebol.jpa.entities.Person;
import cl.blm.trebol.jpa.entities.Seller;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class Seller2Entity
    implements Converter<SellerPojo, Seller> {

  @Override
  public Seller convert(SellerPojo source) {
    Seller target = new Seller(source.getId());
    target.setPerson(new Person(source.getPerson().getId()));
    return target;
  }
}
