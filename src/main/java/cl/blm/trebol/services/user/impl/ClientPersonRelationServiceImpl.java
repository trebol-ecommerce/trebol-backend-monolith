package cl.blm.trebol.services.user.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import com.querydsl.core.types.Predicate;

import cl.blm.trebol.api.pojo.ClientPojo;
import cl.blm.trebol.jpa.entities.Customer;
import cl.blm.trebol.jpa.entities.QCustomer;
import cl.blm.trebol.services.user.ClientPersonRelationService;
import cl.blm.trebol.jpa.repositories.CustomersRepository;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
@Service
public class ClientPersonRelationServiceImpl
    implements ClientPersonRelationService {

  private final CustomersRepository clientsRepository;
  private final ConversionService conversionService;

  @Autowired
  public ClientPersonRelationServiceImpl(CustomersRepository clientsRepository, ConversionService conversionService) {
    this.clientsRepository = clientsRepository;
    this.conversionService = conversionService;
  }

  @Override
  public ClientPojo getClientFromPersonId(int personId) {
    Predicate query = QCustomer.customer.person.id.eq(personId);
    Optional<Customer> foundClient = clientsRepository.findOne(query);
    if (foundClient.isPresent()) {
      Customer entity = foundClient.get();
      ClientPojo target = conversionService.convert(entity, ClientPojo.class);
      return target;
    }
    return null;
  }
}
