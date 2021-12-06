package org.trebol.jpa.services.crud;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.SellStatus;
import org.trebol.jpa.repositories.ISellStatusesJpaRepository;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.SellStatusPojo;

import java.util.Optional;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Transactional
@Service
public class SellStatusesJpaCrudServiceImpl
  extends GenericCrudJpaService<SellStatusPojo, SellStatus> {

  private final ISellStatusesJpaRepository statusesRepository;

  @Autowired
  public SellStatusesJpaCrudServiceImpl(ISellStatusesJpaRepository repository,
                                        ITwoWayConverterJpaService<SellStatusPojo, SellStatus> converter) {
    super(repository,
          converter,
          LoggerFactory.getLogger(SellStatusesJpaCrudServiceImpl.class));
    this.statusesRepository = repository;
  }

  @Override
  public Optional<SellStatus> getExisting(SellStatusPojo input) throws BadInputException {
    String name = input.getName();
    if (name == null || name.isBlank()) {
      throw new BadInputException("Invalid status name");
    } else {
      return statusesRepository.findByName(name);
    }
  }
}
