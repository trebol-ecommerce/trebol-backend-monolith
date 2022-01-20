/*
 * Copyright (c) 2022 The Trebol eCommerce Project
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished
 * to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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

;
@Transactional
@Service
public class SalesJpaCrudServiceImpl
  extends GenericCrudJpaService<SellPojo, Sell> {

  private final ISalesJpaRepository salesRepository;
  private final ITwoWayConverterJpaService<ProductPojo, Product> productConverter;
  private static final double TAX_PERCENT = 0.19; // TODO refactor into a "tax service" of sorts
  private static final boolean CAN_EDIT_AFTER_PROCESS = true; // TODO refactor as part of application properties

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
    this.updateTotals(input);
    Sell output = salesRepository.saveAndFlush(input);
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

  protected SellPojo doUpdate(SellPojo input, Sell existingEntity) throws BadInputException {
    Integer statusCode = existingEntity.getStatus().getCode();
    if ((statusCode >= 3 || statusCode < 0) && !CAN_EDIT_AFTER_PROCESS) {
      throw new BadInputException("The requested transaction cannot be modified");
    }
    Sell updatedEntity = converter.applyChangesToExistingEntity(input, existingEntity);
    this.updateTotals(updatedEntity);
    if (existingEntity.equals(updatedEntity)) {
      return input;
    } else {
      Sell output = salesRepository.saveAndFlush(updatedEntity);
      return converter.convertToPojo(output);
    }
  }

  private void updateTotals(Sell input) {
    int netValue = 0, taxesValue = 0, totalUnits = 0;
    for (SellDetail sd : input.getDetails()) {
      int unitValue = sd.getUnitValue();
      double unitTaxValue = unitValue * TAX_PERCENT;
      double unitNetValue = unitValue - unitTaxValue;
      taxesValue += unitTaxValue;
      netValue += unitNetValue;
      totalUnits += sd.getUnits();
    }
    input.setTaxesValue(taxesValue);
    input.setNetValue(netValue);
    input.setTotalValue(taxesValue + netValue);
    input.setTotalItems(totalUnits);
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
        sellDetails.add(targetSellDetail);
      }
      target.setDetails(sellDetails);
    }
  }
}
