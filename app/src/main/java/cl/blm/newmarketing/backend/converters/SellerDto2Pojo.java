package cl.blm.newmarketing.backend.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.api.pojos.PersonPojo;
import cl.blm.newmarketing.backend.api.pojos.SellerPojo;
import cl.blm.newmarketing.backend.dtos.SellerDto;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class SellerDto2Pojo
    implements Converter<SellerDto, SellerPojo> {
  @Override
  public SellerPojo convert(SellerDto source) {
    SellerPojo target = new SellerPojo();
    target.id = source.getSellerId();

    PersonPojo person = new PersonDto2Pojo().convert(source.getPerson());
    target.person = person;
    return target;
  }
}
