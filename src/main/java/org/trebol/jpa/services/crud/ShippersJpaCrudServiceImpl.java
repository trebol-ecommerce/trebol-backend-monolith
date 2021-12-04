package org.trebol.jpa.services.crud;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Shipper;
import org.trebol.jpa.repositories.IShippersJpaRepository;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.ShipperPojo;

import java.util.Optional;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Transactional
@Service
public class ShippersJpaCrudServiceImpl
  extends GenericCrudJpaService<ShipperPojo, Shipper> {

  private final IShippersJpaRepository shippersRepository;

  @Autowired
  public ShippersJpaCrudServiceImpl(IShippersJpaRepository repository,
                                    ITwoWayConverterJpaService<ShipperPojo, Shipper> converter) {
    super(repository,
          converter,
          LoggerFactory.getLogger(ShippersJpaCrudServiceImpl.class));
    this.shippersRepository = repository;
  }

  @Override
  public Optional<Shipper> getExisting(ShipperPojo input) throws BadInputException {
    String name = input.getName();
    if (name == null || name.isBlank()) {
      throw new BadInputException("Billing type has no name");
    } else {
      return shippersRepository.findByName(name);
    }
  }
}
