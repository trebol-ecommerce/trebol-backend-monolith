package cl.blm.trebol.converters.topojo;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.trebol.api.pojo.SellerPojo;
import cl.blm.trebol.jpa.entities.Seller;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class Seller2Pojo
    implements Converter<Seller, SellerPojo> {

  @Override
  public SellerPojo convert(Seller source) {
    SellerPojo target = new SellerPojo();
    target.setId(source.getId());
    return target;
  }
}
