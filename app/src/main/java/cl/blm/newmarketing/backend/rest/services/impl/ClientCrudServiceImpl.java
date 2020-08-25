package cl.blm.newmarketing.backend.rest.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import cl.blm.newmarketing.backend.model.entities.Client;
import cl.blm.newmarketing.backend.model.entities.QClient;
import cl.blm.newmarketing.backend.model.repositories.ClientsRepository;
import cl.blm.newmarketing.backend.rest.dtos.ClientDto;
import cl.blm.newmarketing.backend.rest.services.CrudService;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Transactional
@Service
public class ClientCrudServiceImpl
    implements CrudService<ClientDto, Integer> {
  private static final Logger LOG = LoggerFactory.getLogger(ClientCrudServiceImpl.class);

  @Autowired
  ClientsRepository clients;
  @Autowired
  ConversionService conversion;

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

  @Nullable
  @Override
  public ClientDto create(ClientDto dto) {
    LOG.debug("create({})", dto);
    Client newEntity = conversion.convert(dto, Client.class);
    if (dto.getClientId() != null && clients.findById(dto.getClientId()).isPresent()) {
      return null;
    } else {
      newEntity = clients.saveAndFlush(newEntity);
      ClientDto newDto = conversion.convert(newEntity, ClientDto.class);
      return newDto;
    }
  }

  @Override
  public Collection<ClientDto> read(int pageSize, int pageIndex, Predicate filters) {
    LOG.debug("read({}, {}, {})", pageSize, pageIndex, filters);
    Sort orden = Sort.by("id").ascending();
    Pageable paged = PageRequest.of(pageIndex, pageSize, orden);

    Iterable<Client> iterable;
    if (filters == null) {
      iterable = clients.findAll(paged);
    } else {
      iterable = clients.findAll(filters, paged);
    }

    List<ClientDto> list = new ArrayList<>();
    for (Client client : iterable) {
      ClientDto dto = conversion.convert(client, ClientDto.class);
      list.add(dto);
    }

    return list;
  }

  @Nullable
  @Override
  public ClientDto update(ClientDto dto) {
    LOG.debug("update({})", dto);
    Optional<Client> queriedClient = clients.findById(dto.getClientId());
    if (!queriedClient.isPresent()) {
      return null;
    } else {
      Client existingClient = queriedClient.get();
      Client newPerson = conversion.convert(dto, Client.class);
      if (newPerson.equals(existingClient)) {
        return dto;
      } else {
        try {
          newPerson = clients.saveAndFlush(newPerson);
          return conversion.convert(newPerson, ClientDto.class);
        } catch (Exception exc) {
          LOG.error("Client could not be saved");
          return null;
        }
      }
    }
  }

  @Nullable
  @Override
  public ClientDto update(ClientDto dto, Integer id) {
    LOG.debug("update({}, {})", dto, id);
    Optional<Client> queriedClient = clients.findById(id);
    if (!queriedClient.isPresent()) {
      return null;
    } else {
      Client existingClient = queriedClient.get();
      Client newClient = conversion.convert(dto, Client.class);
      if (newClient.equals(existingClient)) {
        return dto;
      } else {
        try {
          newClient = clients.saveAndFlush(newClient);
          return conversion.convert(newClient, ClientDto.class);
        } catch (Exception exc) {
          LOG.error("Client could not be saved");
          return null;
        }
      }
    }
  }

  @Override
  public boolean delete(Integer id) {
    LOG.debug("delete({})", id);
    try {
      clients.deleteById(id);
      clients.flush();
      return !clients.existsById(id);
    } catch (Exception exc) {
      LOG.error("Could not delete Client with id {}", id, exc);
      return false;
    }
  }

  @Nullable
  @Override
  public ClientDto find(Integer id) {
    LOG.debug("find({})", id);
    Optional<Client> clientById = clients.findById(id);
    if (!clientById.isPresent()) {
      return null;
    } else {
      Client entity = clientById.get();
      return conversion.convert(entity, ClientDto.class);
    }
  }
}
