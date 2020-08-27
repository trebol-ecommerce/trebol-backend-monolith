package cl.blm.newmarketing.backend.converters.dto2pojo;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.api.pojos.ClientPojo;
import cl.blm.newmarketing.backend.api.pojos.PersonPojo;
import cl.blm.newmarketing.backend.dtos.ClientDto;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class ClientDto2Pojo
    implements Converter<ClientDto, ClientPojo> {
  @Override
  public ClientPojo convert(ClientDto source) {
    ClientPojo target = new ClientPojo();
    target.id = source.getClientId();

    if (source.getPerson() != null) {
      PersonPojo person = (new PersonDto2Pojo()).convert(source.getPerson());
      target.person = person;
    }

    return target;
  }
}
