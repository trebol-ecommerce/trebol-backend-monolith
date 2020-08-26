package cl.blm.newmarketing.backend.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.api.pojos.ClientPojo;
import cl.blm.newmarketing.backend.dtos.ClientDto;
import cl.blm.newmarketing.backend.dtos.PersonDto;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class ClientPojo2Dto
    implements Converter<ClientPojo, ClientDto> {

  @Override
  public ClientDto convert(ClientPojo source) {
    ClientDto target = new ClientDto();
    target.setClientId(source.id);
    PersonDto person = new PersonPojo2Dto().convert(source.person);
    target.setPerson(person);
    return target;
  }
}
