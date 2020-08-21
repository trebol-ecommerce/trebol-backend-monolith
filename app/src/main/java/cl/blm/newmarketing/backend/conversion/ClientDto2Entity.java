package cl.blm.newmarketing.backend.conversion;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.model.entities.Client;
import cl.blm.newmarketing.backend.rest.dtos.ClientDto;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class ClientDto2Entity
    implements Converter<ClientDto, Client> {
  @Override
  public Client convert(ClientDto source) {
    Client target = new Client();
    target.setId(source.getClientId());
    // TODO can bind person too?
    return target;
  }
}
