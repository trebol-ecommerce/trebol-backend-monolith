package org.trebol.jpa.services.crud;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Salesperson;
import org.trebol.jpa.repositories.ISalespeopleJpaRepository;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.PersonPojo;
import org.trebol.pojo.SalespersonPojo;

import java.util.Optional;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Transactional
@Service
public class SalespeopleJpaCrudServiceImpl
  extends GenericCrudJpaService<SalespersonPojo, Salesperson> {

  private final ISalespeopleJpaRepository salespeopleRepository;

  @Autowired
  public SalespeopleJpaCrudServiceImpl(ISalespeopleJpaRepository repository,
                                       ITwoWayConverterJpaService<SalespersonPojo, Salesperson> converter) {
    super(repository,
          converter,
          LoggerFactory.getLogger(SalespeopleJpaCrudServiceImpl.class));
    this.salespeopleRepository = repository;
  }

  @Override
  public Optional<Salesperson> getExisting(SalespersonPojo input) throws BadInputException {
    PersonPojo person = input.getPerson();
    if (person == null) {
      throw new BadInputException("Salesperson does not have profile information");
    } else {
      String idNumber = person.getIdNumber();
      if (idNumber == null) {
        throw new BadInputException("Salesperson does not have an ID card");
      } else {
        return salespeopleRepository.findByPersonIdNumber(idNumber);
      }
    }
  }
}
