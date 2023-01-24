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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.*;
import org.trebol.jpa.repositories.AddressesJpaRepository;
import org.trebol.jpa.repositories.BillingTypesJpaRepository;
import org.trebol.jpa.repositories.ProductsJpaRepository;
import org.trebol.jpa.repositories.SalesJpaRepository;
import org.trebol.jpa.services.CrudGenericService;
import org.trebol.jpa.services.conversion.*;
import org.trebol.jpa.services.datatransport.SalesDataTransportService;
import org.trebol.pojo.*;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.trebol.config.Constants.BILLING_TYPE_ENTERPRISE;

@Transactional
@Service
public class SalesCrudServiceImpl
  extends CrudGenericService<SellPojo, Sell>
  implements SalesCrudService {
  private final SalesJpaRepository salesRepository;
  private final SalesConverterService salesConverterService;
  private final SalesDataTransportService salesDataTransportService;
  private final ProductsJpaRepository productsRepository;
  private final ProductsConverterService productConverterService;
  private final CustomersCrudService customersCrudService;
  private final CustomersConverterService customersConverterService;
  private final BillingTypesJpaRepository billingTypesRepository;
  private final BillingCompaniesCrudService billingCompaniesCrudService;
  private final BillingCompaniesConverterService billingCompaniesConverterService;
  private final AddressesJpaRepository addressesRepository;
  private final ShippersCrudService shippersCrudService;
  private final AddressesConverterService addressesConverterService;
  private static final double TAX_PERCENT = 0.19; // TODO refactor into a "tax service" of sorts
  private static final boolean CAN_EDIT_AFTER_PROCESS = true; // TODO refactor as part of application properties

  @Autowired
  public SalesCrudServiceImpl(
    SalesJpaRepository salesRepository,
    ProductsJpaRepository productsRepository,
    SalesConverterService salesConverterService,
    SalesDataTransportService salesDataTransportService,
    ProductsConverterService productConverterService,
    CustomersCrudService customersCrudService,
    CustomersConverterService customersConverterService,
    BillingTypesJpaRepository billingTypesRepository,
    BillingCompaniesCrudService billingCompaniesCrudService,
    BillingCompaniesConverterService billingCompaniesConverterService,
    AddressesJpaRepository addressesRepository,
    ShippersCrudService shippersCrudService,
    AddressesConverterService addressesConverterService
  ) {
    super(salesRepository, salesConverterService, salesDataTransportService);
    this.salesRepository = salesRepository;
    this.productsRepository = productsRepository;
    this.salesConverterService = salesConverterService;
    this.salesDataTransportService = salesDataTransportService;
    this.productConverterService = productConverterService;
    this.customersCrudService = customersCrudService;
    this.customersConverterService = customersConverterService;
    this.billingTypesRepository = billingTypesRepository;
    this.billingCompaniesCrudService = billingCompaniesCrudService;
    this.billingCompaniesConverterService = billingCompaniesConverterService;
    this.addressesRepository = addressesRepository;
    this.shippersCrudService = shippersCrudService;
    this.addressesConverterService = addressesConverterService;
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
      SellPojo foundPojo = salesConverterService.convertToPojo(found);
      List<SellDetailPojo> detailPojos = this.convertDetailsToPojos(found.getDetails());
      foundPojo.setDetails(detailPojos);
      return foundPojo;
    } else {
      throw new EntityNotFoundException("No sell matches the filtering conditions");
    }
  }

  @Override
  protected SellPojo persistEntityWithUpdatesFromPojo(SellPojo changes, Sell existingEntity)
    throws BadInputException {
    Integer statusCode = existingEntity.getStatus().getCode();
    if ((statusCode >= 3 || statusCode < 0) && !CAN_EDIT_AFTER_PROCESS) {
      throw new BadInputException("The requested transaction cannot be modified");
    }
    Sell updatedEntity = salesDataTransportService.applyChangesToExistingEntity(changes, existingEntity);
    if (updatedEntity.equals(existingEntity)) {
      return changes;
    }
    return this.persist(updatedEntity);
  }

  @Override
  protected Sell prepareNewEntityFromInputPojo(SellPojo inputPojo) throws BadInputException {
    Sell target = salesConverterService.convertToNewEntity(inputPojo);

    CustomerPojo pojoCustomer = inputPojo.getCustomer();
    Optional<Customer> existingCustomer = customersCrudService.getExisting(pojoCustomer);
    if (existingCustomer.isEmpty()) {
      Customer customer = customersConverterService.convertToNewEntity(pojoCustomer);
      target.setCustomer(customer);
    } else {
      target.setCustomer(existingCustomer.get());
    }

    String pojoBillingTypeName = inputPojo.getBillingType();
    Optional<BillingType> existingBillingType = billingTypesRepository.findByName(pojoBillingTypeName);
    if (existingBillingType.isEmpty()) {
      throw new BadInputException("Specified billing type does not exist");
    }
    target.setBillingType(existingBillingType.get());

    if (pojoBillingTypeName.equals(BILLING_TYPE_ENTERPRISE)) {
      BillingCompanyPojo pojoBillingCompany = inputPojo.getBillingCompany();
      Optional<BillingCompany> existingCompany = billingCompaniesCrudService.getExisting(pojoBillingCompany);
      if (existingCompany.isEmpty()) {
        BillingCompany billingCompany = billingCompaniesConverterService.convertToNewEntity(pojoBillingCompany);
        target.setBillingCompany(billingCompany);
      } else {
        target.setBillingCompany(existingCompany.get());
      }
    }

    AddressPojo pojoBillingAddress = inputPojo.getBillingAddress();
    if (pojoBillingAddress != null) {
      Optional<Address> existingBillingAddress = this.findAddress(pojoBillingAddress);
      if (existingBillingAddress.isEmpty()) {
        Address address = addressesConverterService.convertToNewEntity(pojoBillingAddress);
        target.setBillingAddress(address);
      } else {
        target.setBillingAddress(existingBillingAddress.get());
      }
    }

    AddressPojo pojoShippingAddress = inputPojo.getShippingAddress();
    if (pojoShippingAddress != null) {
      Optional<Address> existingShippingAddress = this.findAddress(pojoShippingAddress);
      if (existingShippingAddress.isEmpty()) {
        Address address = addressesConverterService.convertToNewEntity(pojoShippingAddress);
        target.setShippingAddress(address);
      } else {
        target.setShippingAddress(existingShippingAddress.get());
      }
      ShipperPojo pojoShipper = inputPojo.getShipper();
      Optional<Shipper> existingShipper = shippersCrudService.getExisting(pojoShipper);
      if (existingShipper.isEmpty()) {
        throw new BadInputException("Specified shipper does not exist");
      }
      target.setShipper(existingShipper.get());
    }

    List<SellDetail> detailEntities = this.convertDetailsToEntities(inputPojo.getDetails());
    target.setDetails(detailEntities);
    this.updateTotals(target);
    return target;
  }

  private Optional<Address> findAddress(AddressPojo address) {
    return addressesRepository.findByFields(
      address.getCity(),
      address.getMunicipality(),
      address.getFirstLine(),
      address.getSecondLine(),
      address.getPostalCode(),
      address.getNotes()
    );
  }

  private List<SellDetail> convertDetailsToEntities(Collection<SellDetailPojo> sourceDetails) throws BadInputException {
    List<SellDetail> details = new ArrayList<>();
    for (SellDetailPojo d : sourceDetails) {
      String barcode = d.getProduct().getBarcode();
      if (StringUtils.isBlank(barcode)) {
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
      ProductPojo product = productConverterService.convertToPojo(sourceSellDetail.getProduct());
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
