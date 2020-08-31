package cl.blm.newmarketing.backend.converters.topojo;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.api.pojo.PersonPojo;
import cl.blm.newmarketing.backend.api.pojo.SellerPojo;
import cl.blm.newmarketing.backend.model.entities.Seller;

@Component
public class Seller2Pojo
    implements Converter<Seller, SellerPojo> {

  @Override
  public SellerPojo convert(Seller source) {
    SellerPojo target = new SellerPojo();
    target.id = source.getId();
    target.person = new PersonPojo();
    target.person.id = source.getPerson().getId();
    return target;
  }
}
