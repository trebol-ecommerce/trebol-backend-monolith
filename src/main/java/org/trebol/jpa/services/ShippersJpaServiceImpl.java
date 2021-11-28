package org.trebol.jpa.services;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.GenericJpaService;
import org.trebol.jpa.entities.Shipper;
import org.trebol.jpa.entities.QShipper;
import org.trebol.jpa.repositories.IShippersJpaRepository;
import org.trebol.pojo.ShipperPojo;

import java.util.Map;
import java.util.Optional;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Service
public class ShippersJpaServiceImpl
  extends GenericJpaService<ShipperPojo, Shipper> {

  private final IShippersJpaRepository billingTypesRepository;
  private final ConversionService conversion;

  @Autowired
  public ShippersJpaServiceImpl(IShippersJpaRepository repository, ConversionService conversion) {
    super(repository, LoggerFactory.getLogger(ShippersJpaServiceImpl.class));
    this.billingTypesRepository = repository;
    this.conversion = conversion;
  }

  @Override
  public Optional<Shipper> getExisting(ShipperPojo input) throws BadInputException {
    String name = input.getName();
    if (name == null || name.isBlank()) {
      throw new BadInputException("Billing type has no name");
    } else {
      return billingTypesRepository.findByName(name);
    }
  }

  @Override
  public ShipperPojo convertToPojo(Shipper source) {
    return conversion.convert(source, ShipperPojo.class);
  }

  @Override
  public Shipper convertToNewEntity(ShipperPojo source) {
    Shipper target = new Shipper();
    target.setName(source.getName());
    return target;
  }

  @Override
  public Shipper applyChangesToExistingEntity(ShipperPojo source, Shipper existing) throws BadInputException {
    Shipper target = new Shipper(existing);

    String name = source.getName();
    if (name != null && !name.isBlank() && !target.getName().equals(name)) {
      target.setName(name);
    }

    return target;
  }

  @Override
  public Predicate parsePredicate(Map<String, String> queryParamsMap) {
    QShipper qShipper = QShipper.shipper;
    BooleanBuilder predicate = new BooleanBuilder();
    for (Map.Entry<String, String> entry : queryParamsMap.entrySet()) {
      String paramName = entry.getKey();
      String stringValue = entry.getValue();
      try {
        switch (paramName) {
          case "id":
            return qShipper.id.eq(Long.valueOf(stringValue));
          case "name":
            return qShipper.name.eq(stringValue);
          case "nameLike":
            predicate.and(qShipper.name.likeIgnoreCase("%" + stringValue + "%"));
            break;
          default:
            break;
        }
      } catch (NumberFormatException exc) {
        logger.info("Param '{}' couldn't be parsed as number (value: '{}')", paramName, stringValue);
      }
    }

    return predicate;
  }
}
