package cl.blm.newmarketing.backend.services.data.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import cl.blm.newmarketing.backend.model.entities.Client;
import cl.blm.newmarketing.backend.model.entities.QClient;
import cl.blm.newmarketing.backend.model.repositories.ClientsRepository;
import cl.blm.newmarketing.backend.services.data.GenericDataService;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Transactional
@Service
public class ClientDataServiceImpl
    extends GenericDataService<Client, Integer> {
  private static final Logger LOG = LoggerFactory.getLogger(ClientDataServiceImpl.class);

  private ClientsRepository repository;

  @Autowired
  public ClientDataServiceImpl(ClientsRepository repository) {
    super(LOG, repository);
    this.repository = repository;
  }

  @Override
  public Predicate queryParamsMapToPredicate(Map<String, String> queryParamsMap) {
    LOG.debug("queryParamsMapToPredicate({})", queryParamsMap);
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
        LOG.error("Param '{}' couldn't be parsed as number (value: '{}')", paramName, stringValue, exc);
      }
    }

    return predicate;
  }

  @Override
  public List<Client> read(int pageSize, int pageIndex, Predicate filters) {
    LOG.debug("read({}, {}, {})", pageSize, pageIndex, filters);
    Sort orden = Sort.by("id").ascending();
    Pageable paged = PageRequest.of(pageIndex, pageSize, orden);

    Page<Client> iterable;
    if (filters == null) {
      iterable = repository.deepReadAll(paged);
    } else {
      iterable = repository.deepReadAll(filters, paged);
    }

    return iterable.getContent();
  }
}
