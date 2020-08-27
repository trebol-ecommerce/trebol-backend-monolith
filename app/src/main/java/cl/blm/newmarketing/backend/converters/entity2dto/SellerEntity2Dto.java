package cl.blm.newmarketing.backend.converters.entity2dto;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.dtos.PersonDto;
import cl.blm.newmarketing.backend.dtos.SellerDto;
import cl.blm.newmarketing.backend.model.entities.Seller;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class SellerEntity2Dto
    implements Converter<Seller, SellerDto> {
  @Override
  public SellerDto convert(Seller source) {
    SellerDto target = new SellerDto();
    target.setSellerId(source.getId());

    PersonDto person = (new PersonEntity2Dto()).convert(source.getPerson());
    target.setPerson(person);

    return target;
  }
}
