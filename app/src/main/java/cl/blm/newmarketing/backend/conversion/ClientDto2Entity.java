package cl.blm.newmarketing.backend.conversion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.model.entities.Client;
import cl.blm.newmarketing.backend.model.entities.Person;
import cl.blm.newmarketing.backend.rest.dtos.ClientDto;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class ClientDto2Entity
    implements Converter<ClientDto, Client> {
  @Autowired
  private ConversionService conversion;

  @Override
  public Client convert(ClientDto source) {
    Client target = new Client();
    target.setId(source.getClientId());
    if (source.getPerson() != null) {
      Person person = conversion.convert(source.getPerson(), Person.class);
      target.setPerson(person);
    }
    return target;
  }
}
