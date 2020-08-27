package cl.blm.newmarketing.backend.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.api.pojos.SellerPojo;
import cl.blm.newmarketing.backend.dtos.PersonDto;
import cl.blm.newmarketing.backend.dtos.SellerDto;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class SellerPojo2Dto
    implements Converter<SellerPojo, SellerDto> {
  @Override
  public SellerDto convert(SellerPojo source) {
    SellerDto target = new SellerDto();
    target.setSellerId(source.id);

    PersonDto person = (new PersonPojo2Dto()).convert(source.person);
    target.setPerson(person);
    return target;
  }
}
