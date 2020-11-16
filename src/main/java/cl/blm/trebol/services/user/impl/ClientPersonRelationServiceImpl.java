package cl.blm.trebol.services.user.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import com.querydsl.core.types.Predicate;

import cl.blm.trebol.api.pojo.ClientPojo;
import cl.blm.trebol.jpa.entities.Client;
import cl.blm.trebol.jpa.entities.QClient;
import cl.blm.trebol.jpa.repositories.ClientsRepository;
import cl.blm.trebol.services.user.ClientPersonRelationService;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
@Service
public class ClientPersonRelationServiceImpl
    implements ClientPersonRelationService {

  private final ClientsRepository clientsRepository;
  private final ConversionService conversionService;

  @Autowired
  public ClientPersonRelationServiceImpl(ClientsRepository clientsRepository, ConversionService conversionService) {
    this.clientsRepository = clientsRepository;
    this.conversionService = conversionService;
  }

  @Override
  public ClientPojo getClientFromPersonId(int personId) {
    Predicate query = QClient.client.person.id.eq(personId);
    Optional<Client> foundClient = clientsRepository.findOne(query);
    if (foundClient.isPresent()) {
      Client entity = foundClient.get();
      ClientPojo target = conversionService.convert(entity, ClientPojo.class);
      return target;
    }
    return null;
  }
}
