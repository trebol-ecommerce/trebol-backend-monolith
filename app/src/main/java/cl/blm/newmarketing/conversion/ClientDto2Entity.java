package cl.blm.newmarketing.conversion;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.model.entities.Client;
import cl.blm.newmarketing.rest.dtos.ClientDto;

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
