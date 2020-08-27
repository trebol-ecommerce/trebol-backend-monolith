package cl.blm.newmarketing.backend.converters.dto2entity;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.dtos.SellerDto;
import cl.blm.newmarketing.backend.model.entities.Person;
import cl.blm.newmarketing.backend.model.entities.Seller;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class SellerDto2Entity
    implements Converter<SellerDto, Seller> {
  @Override
  public Seller convert(SellerDto source) {
    Seller target = new Seller();
    target.setId(source.getSellerId());

    if (source.getPerson() != null) {
      Person person = (new PersonDto2Entity()).convert(source.getPerson());
      target.setPerson(person);
    }

    return target;
  }
}
