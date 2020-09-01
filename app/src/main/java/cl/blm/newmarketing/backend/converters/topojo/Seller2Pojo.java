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
    target.setId(source.getId());
    target.setPerson(new PersonPojo());
    target.getPerson().setId(source.getPerson().getId());
    target.getPerson().setName(source.getPerson().getName());
    return target;
  }
}
