package cl.blm.trebol.store.services.crud.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import cl.blm.trebol.store.api.pojo.ClientPojo;
import cl.blm.trebol.store.api.pojo.PersonPojo;
import cl.blm.trebol.store.jpa.entities.Client;
import cl.blm.trebol.store.jpa.entities.Person;
import cl.blm.newmarketing.store.jpa.entities.QClient;
import cl.blm.trebol.store.jpa.repositories.ClientsRepository;
import cl.blm.trebol.store.services.crud.GenericEntityCrudService;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Transactional
@Service
public class ClientCrudServiceImpl
    extends GenericEntityCrudService<ClientPojo, Client, Integer> {
  private static final Logger LOG = LoggerFactory.getLogger(ClientCrudServiceImpl.class);

  private final ClientsRepository repository;
  private final ConversionService conversion;

  @Autowired
  public ClientCrudServiceImpl(ClientsRepository repository, ConversionService conversion) {
    super(repository);
    this.repository = repository;
    this.conversion = conversion;
  }

  @Override
  public ClientPojo entity2Pojo(Client source) {
    ClientPojo target = conversion.convert(source, ClientPojo.class);
    PersonPojo person = conversion.convert(source.getPerson(), PersonPojo.class);
    target.setPerson(person);
    return target;
  }

  @Override
  public Client pojo2Entity(ClientPojo source) {
    Client target = conversion.convert(source, Client.class);
    Person personTarget = conversion.convert(source.getPerson(), Person.class);
    target.setPerson(personTarget);
    return target;
  }

  @Override
  public Page<Client> getAllEntities(Pageable paged, Predicate filters) {
    if (filters == null) {
      return repository.deepReadAll(paged);
    } else {
      return repository.deepReadAll(filters, paged);
    }
  }

  @Override
  public Predicate queryParamsMapToPredicate(Map<String, String> queryParamsMap) {
    QClient qClient = QClient.client;
    BooleanBuilder predicate = new BooleanBuilder();
    for (String paramName : queryParamsMap.keySet()) {
      String stringValue = queryParamsMap.get(paramName);
      try {
        Integer intValue;
        switch (paramName) {
        case "id":
          intValue = Integer.valueOf(stringValue);
          return predicate.and(qClient.id.eq(intValue)); // id matching is final
        case "name":
          predicate.and(qClient.person.name.likeIgnoreCase("%" + stringValue + "%"));
          break;
        case "idnumber":
          predicate.and(qClient.person.idCard.likeIgnoreCase("%" + stringValue + "%"));
          break;
        case "email":
          predicate.and(qClient.person.email.likeIgnoreCase("%" + stringValue + "%"));
          break;
        default:
          break;
        }
      } catch (NumberFormatException exc) {
        LOG.warn("Param '{}' couldn't be parsed as number (value: '{}')", paramName, stringValue, exc);
      }
    }

    return predicate;
  }
}
