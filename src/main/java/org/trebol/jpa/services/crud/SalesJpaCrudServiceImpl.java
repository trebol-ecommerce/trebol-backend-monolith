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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Product;
import org.trebol.jpa.entities.Sell;
import org.trebol.jpa.entities.SellDetail;
import org.trebol.jpa.repositories.IProductsJpaRepository;
import org.trebol.jpa.repositories.ISalesJpaRepository;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.IDataTransportJpaService;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.ProductPojo;
import org.trebol.pojo.SellDetailPojo;
import org.trebol.pojo.SellPojo;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class SalesJpaCrudServiceImpl
  extends GenericCrudJpaService<SellPojo, Sell> {

  private final ISalesJpaRepository salesRepository;
  private final IProductsJpaRepository productsRepository;
  private final ITwoWayConverterJpaService<ProductPojo, Product> productConverter;
  private static final double TAX_PERCENT = 0.19; // TODO refactor into a "tax service" of sorts
  private static final boolean CAN_EDIT_AFTER_PROCESS = true; // TODO refactor as part of application properties

  @Autowired
  public SalesJpaCrudServiceImpl(ISalesJpaRepository repository,
                                 IProductsJpaRepository productsRepository,
                                 ITwoWayConverterJpaService<SellPojo, Sell> converter,
                                 IDataTransportJpaService<SellPojo, Sell> dataTransportService,
                                 ITwoWayConverterJpaService<ProductPojo, Product> productConverter) {
    super(repository,
          converter,
          dataTransportService);
    this.salesRepository = repository;
    this.productsRepository = productsRepository;
    this.productConverter = productConverter;
  }

  @Override
  public Optional<Sell> getExisting(SellPojo input) {
    Long buyOrder = input.getBuyOrder();
    if (buyOrder == null) {
      return Optional.empty();
    } else {
      return this.salesRepository.findById(buyOrder);
    }
  }

  @Override
  public SellPojo readOne(Predicate conditions)
      throws EntityNotFoundException {
    Optional<Sell> matchingSell = salesRepository.findOne(conditions);
    if (matchingSell.isPresent()) {
      Sell found = matchingSell.get();
      SellPojo foundPojo = converter.convertToPojo(found);
      List<SellDetailPojo> detailPojos = this.convertDetailsToPojos(found.getDetails());
      foundPojo.setDetails(detailPojos);
      return foundPojo;
    } else {
      throw new EntityNotFoundException("No sell matches the filtering conditions");
    }
  }

  @Override
  protected Sell prepareEntityWithUpdatesFromPojo(SellPojo changes, Sell existingEntity)
      throws BadInputException {
    Integer statusCode = existingEntity.getStatus().getCode();
    if ((statusCode >= 3 || statusCode < 0) && !CAN_EDIT_AFTER_PROCESS) {
      throw new BadInputException("The requested transaction cannot be modified");
    }
    Sell updatedEntity = dataTransportService.applyChangesToExistingEntity(changes, existingEntity);
    List<SellDetail> detailEntities = this.convertDetailsToEntities(changes.getDetails());
    updatedEntity.setDetails(detailEntities);
    this.updateTotals(updatedEntity);
    return updatedEntity;
  }

  @Override
  public Sell prepareNewEntityFromInputPojo(SellPojo inputPojo) throws BadInputException {
    Sell target = converter.convertToNewEntity(inputPojo);
    this.updateTotals(target);
    return target;
  }

  private List<SellDetail> convertDetailsToEntities(Collection<SellDetailPojo> sourceDetails) throws BadInputException {
    List<SellDetail> details = new ArrayList<>();
    for (SellDetailPojo d : sourceDetails) {
      String barcode = d.getProduct().getBarcode();
      if (barcode == null || barcode.isBlank()) {
        throw new BadInputException("Product barcode must be valid");
      }
      Optional<Product> productByBarcode = productsRepository.findByBarcode(barcode);
      if (productByBarcode.isEmpty()) {
        throw new BadInputException("Unexisting product in sell details");
      }
      Product product = productByBarcode.get();
      SellDetail targetDetail = new SellDetail(d.getUnits(), product);
      targetDetail.setUnitValue(product.getPrice());
      details.add(targetDetail);
    }
    return details;
  }

  private List<SellDetailPojo> convertDetailsToPojos(Collection<SellDetail> details) {
    List<SellDetailPojo> sellDetails = new ArrayList<>();
    for (SellDetail sourceSellDetail : details) {
      ProductPojo product = productConverter.convertToPojo(sourceSellDetail.getProduct());
      SellDetailPojo targetSellDetail = SellDetailPojo.builder()
        .id(sourceSellDetail.getId())
        .unitValue(sourceSellDetail.getUnitValue())
        .units(sourceSellDetail.getUnits())
        .product(product)
        .description(sourceSellDetail.getDescription())
        .build();
      sellDetails.add(targetSellDetail);
    }
    return sellDetails;
  }

  private void updateTotals(Sell input) {
    int netValue = 0, taxesValue = 0, totalUnits = 0;
    for (SellDetail sd : input.getDetails()) {
      int unitValue = sd.getUnitValue();
      double unitTaxValue = unitValue * TAX_PERCENT;
      double unitNetValue = unitValue - unitTaxValue;
      taxesValue += (unitTaxValue * sd.getUnits());
      netValue += (unitNetValue * sd.getUnits());
      totalUnits += sd.getUnits();
    }
    input.setTaxesValue(taxesValue);
    input.setNetValue(netValue);
    input.setTotalValue(taxesValue + netValue);
    input.setTotalItems(totalUnits);
  }
}
