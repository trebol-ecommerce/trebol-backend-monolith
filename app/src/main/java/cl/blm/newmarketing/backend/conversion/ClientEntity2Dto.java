package cl.blm.newmarketing.backend.conversion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.model.entities.Client;
import cl.blm.newmarketing.backend.rest.dtos.ClientDto;
import cl.blm.newmarketing.backend.rest.dtos.PersonDto;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class ClientEntity2Dto
    implements Converter<Client, ClientDto> {
  @Autowired
  private ConversionService conversion;

  @Override
  public ClientDto convert(Client source) {
    ClientDto target = new ClientDto();
    target.setClientId(source.getId());
    PersonDto person = conversion.convert(source.getPerson(), PersonDto.class);
    target.setPerson(person);
    return target;
  }
}
