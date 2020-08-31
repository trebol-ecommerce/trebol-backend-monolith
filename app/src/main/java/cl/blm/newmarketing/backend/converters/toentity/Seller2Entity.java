package cl.blm.newmarketing.backend.converters.toentity;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.api.pojo.SellerPojo;
import cl.blm.newmarketing.backend.model.entities.Person;
import cl.blm.newmarketing.backend.model.entities.Seller;

@Component
public class Seller2Entity
    implements Converter<SellerPojo, Seller> {

  @Override
  public Seller convert(SellerPojo source) {
    Seller target = new Seller(source.id);
    target.setPerson(new Person(source.person.id));
    return target;
  }
}
