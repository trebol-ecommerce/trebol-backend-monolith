package org.trebol.jpa.services.crud;

import com.querydsl.core.types.Predicate;
import javassist.NotFoundException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.exceptions.BadInputException;
import org.trebol.exceptions.EntityAlreadyExistsException;
import org.trebol.jpa.entities.Product;
import org.trebol.jpa.entities.Sell;
import org.trebol.jpa.entities.SellDetail;
import org.trebol.jpa.repositories.ISalesJpaRepository;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.ProductPojo;
import org.trebol.pojo.SellDetailPojo;
import org.trebol.pojo.SellPojo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Transactional
@Service
public class SalesJpaCrudServiceImpl
  extends GenericCrudJpaService<SellPojo, Sell> {

  private final ISalesJpaRepository salesRepository;
  private final ITwoWayConverterJpaService<ProductPojo, Product> productConverter;

  @Autowired
  public SalesJpaCrudServiceImpl(ISalesJpaRepository repository,
                                 ITwoWayConverterJpaService<SellPojo, Sell> converter,
                                 ITwoWayConverterJpaService<ProductPojo, Product> productConverter) {
    super(repository,
          converter,
          LoggerFactory.getLogger(SalesJpaCrudServiceImpl.class));
    this.salesRepository = repository;
    this.productConverter = productConverter;
  }

  @Override
  public SellPojo create(SellPojo inputPojo) throws BadInputException, EntityAlreadyExistsException {
    Sell input = converter.convertToNewEntity(inputPojo);
    Sell output = repository.saveAndFlush(input);
    return converter.convertToPojo(output);
  }

  @Override
  public Optional<Sell> getExisting(SellPojo input) throws BadInputException {
    Long buyOrder = input.getBuyOrder();
    if (buyOrder == null) {
      throw new BadInputException("Invalid buy order.");
    } else {
      return this.salesRepository.findById(buyOrder);
    }
  }

  @Override
  public SellPojo readOne(Predicate conditions) throws NotFoundException {
    Optional<Sell> matchingSell = salesRepository.findOne(conditions);
    if (matchingSell.isPresent()) {
      Sell found = matchingSell.get();
      SellPojo foundPojo = converter.convertToPojo(found);
      this.applyDetails(found, foundPojo);
      return foundPojo;
    } else {
      throw new NotFoundException("No sell matches the filtering conditions");
    }
  }

  private void applyDetails(Sell source, SellPojo target) {
    Collection<SellDetail> details = source.getDetails();
    if (details != null && !details.isEmpty()) {
      List<SellDetailPojo> sellDetails = new ArrayList<>();
      for (SellDetail sourceSellDetail : details) {
        ProductPojo product = productConverter.convertToPojo(sourceSellDetail.getProduct());
        SellDetailPojo targetSellDetail = new SellDetailPojo();
        targetSellDetail.setId(sourceSellDetail.getId());
        targetSellDetail.setUnitValue(sourceSellDetail.getUnitValue());
        targetSellDetail.setUnits(sourceSellDetail.getUnits());
        targetSellDetail.setProduct(product);
        if (product != null) {
          sellDetails.add(targetSellDetail);
        }
      }
      target.setDetails(sellDetails);
    }
  }
}
