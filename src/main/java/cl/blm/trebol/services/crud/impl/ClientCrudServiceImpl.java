package cl.blm.trebol.services.crud.impl;

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

import cl.blm.trebol.jpa.entities.QCustomer;
import cl.blm.trebol.api.pojo.ClientPojo;
import cl.blm.trebol.api.pojo.PersonPojo;
import cl.blm.trebol.jpa.entities.Customer;
import cl.blm.trebol.jpa.entities.Person;
import cl.blm.trebol.services.crud.GenericCrudService;
import cl.blm.trebol.jpa.repositories.CustomersRepository;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Transactional
@Service
public class ClientCrudServiceImpl
    extends GenericCrudService<ClientPojo, Customer, Integer> {
  private static final Logger LOG = LoggerFactory.getLogger(ClientCrudServiceImpl.class);

  private final CustomersRepository repository;
  private final ConversionService conversion;

  @Autowired
  public ClientCrudServiceImpl(CustomersRepository repository, ConversionService conversion) {
    super(repository);
    this.repository = repository;
    this.conversion = conversion;
  }

  @Override
  public ClientPojo entity2Pojo(Customer source) {
    ClientPojo target = conversion.convert(source, ClientPojo.class);
    PersonPojo person = conversion.convert(source.getPerson(), PersonPojo.class);
    target.setPerson(person);
    return target;
  }

  @Override
  public Customer pojo2Entity(ClientPojo source) {
    Customer target = conversion.convert(source, Customer.class);
    Person personTarget = conversion.convert(source.getPerson(), Person.class);
    target.setPerson(personTarget);
    return target;
  }

  @Override
  public Page<Customer> getAllEntities(Pageable paged, Predicate filters) {
    if (filters == null) {
      return repository.deepReadAll(paged);
    } else {
      return repository.findAll(filters, paged);
    }
  }

  @Override
  public Predicate queryParamsMapToPredicate(Map<String, String> queryParamsMap) {
    QCustomer qCustomer = QCustomer.customer;
    BooleanBuilder predicate = new BooleanBuilder();
    for (String paramName : queryParamsMap.keySet()) {
      String stringValue = queryParamsMap.get(paramName);
      try {
        Integer intValue;
        switch (paramName) {
          case "id":
            intValue = Integer.valueOf(stringValue);
            return predicate.and(qCustomer.id.eq(intValue)); // id matching is final
          case "name":
            predicate.and(qCustomer.person.name.likeIgnoreCase("%" + stringValue + "%"));
            break;
          case "idnumber":
            predicate.and(qCustomer.person.idCard.likeIgnoreCase("%" + stringValue + "%"));
            break;
          case "email":
            predicate.and(qCustomer.person.email.likeIgnoreCase("%" + stringValue + "%"));
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
