/*
 * Copyright (c) 2023 The Trebol eCommerce Project
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

package org.trebol.jpa.services.conversion.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.api.models.AddressPojo;
import org.trebol.api.models.BillingCompanyPojo;
import org.trebol.api.models.CustomerPojo;
import org.trebol.api.models.ProductPojo;
import org.trebol.api.models.SalespersonPojo;
import org.trebol.api.models.SellDetailPojo;
import org.trebol.api.models.SellPojo;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.jpa.entities.Address;
import org.trebol.jpa.entities.BillingCompany;
import org.trebol.jpa.entities.BillingType;
import org.trebol.jpa.entities.Customer;
import org.trebol.jpa.entities.Product;
import org.trebol.jpa.entities.Sell;
import org.trebol.jpa.entities.SellDetail;
import org.trebol.jpa.entities.Shipper;
import org.trebol.jpa.repositories.AddressesRepository;
import org.trebol.jpa.repositories.BillingTypesRepository;
import org.trebol.jpa.repositories.ProductsRepository;
import org.trebol.jpa.repositories.ShippersRepository;
import org.trebol.jpa.services.conversion.AddressesConverterService;
import org.trebol.jpa.services.conversion.BillingCompaniesConverterService;
import org.trebol.jpa.services.conversion.CustomersConverterService;
import org.trebol.jpa.services.conversion.ProductsConverterService;
import org.trebol.jpa.services.conversion.SalesConverterService;
import org.trebol.jpa.services.conversion.SalespeopleConverterService;
import org.trebol.jpa.services.crud.BillingCompaniesCrudService;
import org.trebol.jpa.services.crud.CustomersCrudService;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.trebol.config.Constants.BILLING_TYPE_ENTERPRISE;

@Transactional
@Service
public class SalesConverterServiceImpl
  implements SalesConverterService {
  private final CustomersCrudService customersCrudService;
  private final CustomersConverterService customersConverterService;
  private final BillingTypesRepository billingTypesRepository;
  private final BillingCompaniesCrudService billingCompaniesCrudService;
  private final BillingCompaniesConverterService billingCompaniesConverterService;
  private final SalespeopleConverterService salespeopleConverterService;
  private final ProductsConverterService productConverterService;
  private final ProductsRepository productsRepository;
  private final ShippersRepository shippersRepository;
  private final AddressesRepository addressesRepository;
  private final AddressesConverterService addressesConverterService;
  private static final double TAX_PERCENT = 0.19; // TODO refactor into a "tax service" of sorts
  static final String UNEXISTING_BILLING_TYPE = "Specified billing type does not exist";

  @Autowired
  public SalesConverterServiceImpl(
    CustomersCrudService customersCrudService,
    CustomersConverterService customersConverterService,
    BillingTypesRepository billingTypesRepository,
    BillingCompaniesCrudService billingCompaniesCrudService,
    BillingCompaniesConverterService billingCompaniesConverterService,
    SalespeopleConverterService salespeopleConverterService,
    ProductsConverterService productConverterService,
    ProductsRepository productsRepository,
    ShippersRepository shippersRepository,
    AddressesRepository addressesRepository,
    AddressesConverterService addressesConverterService
  ) {
    this.customersCrudService = customersCrudService;
    this.customersConverterService = customersConverterService;
    this.billingTypesRepository = billingTypesRepository;
    this.billingCompaniesCrudService = billingCompaniesCrudService;
    this.billingCompaniesConverterService = billingCompaniesConverterService;
    this.salespeopleConverterService = salespeopleConverterService;
    this.productConverterService = productConverterService;
    this.productsRepository = productsRepository;
    this.shippersRepository = shippersRepository;
    this.addressesRepository = addressesRepository;
    this.addressesConverterService = addressesConverterService;
  }

  @Override
  public SellPojo convertToPojo(Sell source) {
    SellPojo target = SellPojo.builder()
      .buyOrder(source.getId())
      .date(source.getDate())
      .netValue(source.getNetValue())
      .taxValue(source.getTaxesValue())
      .totalValue(source.getTotalValue())
      .totalItems(source.getTotalItems())
      .transportValue(source.getTransportValue())
      .token(source.getTransactionToken())
      .build();

    CustomerPojo customer = customersConverterService.convertToPojo(source.getCustomer());
    target.setCustomer(customer);

    target.setStatus(source.getStatus().getName());
    target.setPaymentType(source.getPaymentType().getName());
    target.setBillingType(source.getBillingType().getName());

    if (target.getBillingType().equals(BILLING_TYPE_ENTERPRISE)) {
      BillingCompany sourceBillingCompany = source.getBillingCompany();
      if (sourceBillingCompany != null) {
        BillingCompanyPojo targetBillingCompany = billingCompaniesConverterService.convertToPojo(sourceBillingCompany);
        target.setBillingCompany(targetBillingCompany);
      }
    }

    if (source.getShipper() != null) {
      target.setShipper(source.getShipper().getName());
    }

    if (source.getSalesperson() != null) {
      SalespersonPojo salesperson = salespeopleConverterService.convertToPojo(source.getSalesperson());
      target.setSalesperson(salesperson);
    }
    return target;
  }

  /**
   * Converts a brand-new sell input object into a fully-fledged entity equivalent.<br/>
   * It validates that every piece of information on it is valid and cohesive.
   * For that, it makes several database queries in the process.
   */
  @Override
  public Sell convertToNewEntity(SellPojo model) throws BadInputException {
    Sell target = Sell.builder().build();
    // databases usually can take care of null dates. besides, the field is annotated with @CreationTimeStamp
    if (model.getDate() != null) {
      target.setDate(model.getDate());
    }
    this.convertCustomerInformationForEntity(model, target);
    this.convertBillingInformationForEntity(model, target);
    this.convertShippingInformationForEntity(model, target);
    this.convertDetailsForEntity(model, target);
    this.updateTotals(target);
    return target;
  }

  @Override
  public SellDetailPojo convertDetailToPojo(SellDetail source) {
    Product sourceProduct = source.getProduct();
    ProductPojo product = productConverterService.convertToPojo(sourceProduct);
    return SellDetailPojo.builder()
      .unitValue(source.getUnitValue())
      .units(source.getUnits())
      .product(product)
      .description(source.getDescription())
      .build();
  }

  /**
   * Converts a detail of the sell into a new entity counterpart.<br/>
   * Its product is matched against the records via its barcode.
   * The value of the unit is set the same as the product's.
   */
  @Override
  public SellDetail convertDetailToNewEntity(SellDetailPojo detail) throws RuntimeException {
    try {
      String barcode = detail.getProduct().getBarcode();
      if (StringUtils.isBlank(barcode)) {
        throw new BadInputException("Product barcode must be valid");
      }
      Optional<Product> productByBarcode = productsRepository.findByBarcode(barcode);
      if (productByBarcode.isEmpty()) {
        throw new BadInputException("Unexisting product in sell details");
      }
      Product product = productByBarcode.get();
      return SellDetail.builder()
        .units(detail.getUnits())
        .product(product)
        .unitValue(product.getPrice())
        .build();
    } catch (BadInputException exc) {
      throw new RuntimeException(exc.getMessage(), exc);
    }
  }

  @Override
  public Sell applyChangesToExistingEntity(SellPojo source, Sell target) {
    throw new UnsupportedOperationException("This method is deprecated");
  }

  /**
   * Tries to find a matching customer from the info placed in the order.<br/>
   * If it can't find any match, it will convert the data into a new customer.
   * Either way, the sell will have the customer set.
   */
  private void convertCustomerInformationForEntity(SellPojo model, Sell target) throws BadInputException {
    CustomerPojo pojoCustomer = model.getCustomer();
    Optional<Customer> existingCustomer = customersCrudService.getExisting(pojoCustomer);
    if (existingCustomer.isEmpty()) {
      Customer customer = customersConverterService.convertToNewEntity(pojoCustomer);
      target.setCustomer(customer);
    } else {
      target.setCustomer(existingCustomer.get());
    }
  }

  /**
   * Tries to deduce billing information.<br/>
   * If the customer requested an invoice for a business company, it will try to match said company
   * against the existing records, or create a new record for that company.<br/>
   * The same process goes for the billing address.
   */
  private void convertBillingInformationForEntity(SellPojo model, Sell target) throws BadInputException {
    String pojoBillingTypeName = model.getBillingType();
    Optional<BillingType> existingBillingType = billingTypesRepository.findByName(pojoBillingTypeName);
    if (existingBillingType.isEmpty()) {
      throw new BadInputException(UNEXISTING_BILLING_TYPE);
    }
    target.setBillingType(existingBillingType.get());

    if (pojoBillingTypeName.equals(BILLING_TYPE_ENTERPRISE)) {
      BillingCompanyPojo pojoBillingCompany = model.getBillingCompany();
      Optional<BillingCompany> existingCompany = billingCompaniesCrudService.getExisting(pojoBillingCompany);
      if (existingCompany.isEmpty()) {
        BillingCompany billingCompany = billingCompaniesConverterService.convertToNewEntity(pojoBillingCompany);
        target.setBillingCompany(billingCompany);
      } else {
        target.setBillingCompany(existingCompany.get());
      }
    }
    AddressPojo pojoBillingAddress = model.getBillingAddress();
    Optional<Address> existingBillingAddress = this.findAddress(pojoBillingAddress);
    if (existingBillingAddress.isEmpty()) {
      Address address = addressesConverterService.convertToNewEntity(pojoBillingAddress);
      target.setBillingAddress(address);
    } else {
      target.setBillingAddress(existingBillingAddress.get());
    }
  }

  /**
   * Checks whether the order included shipping information, and if so, tries to deduce it.<br/>
   * Tries to find the requested shipper, and the shipping address.
   * In the case of the address only, it may create a new record.
   */
  private void convertShippingInformationForEntity(SellPojo model, Sell target) throws BadInputException {
    String pojoShipperName = model.getShipper();
    AddressPojo pojoShippingAddress = model.getShippingAddress();
    if (!StringUtils.isBlank(pojoShipperName) && pojoShippingAddress != null) {
      Optional<Shipper> existingShipper = shippersRepository.findByName(pojoShipperName);
      if (existingShipper.isEmpty()) {
        throw new BadInputException("Specified shipper does not exist");
      }
      target.setShipper(existingShipper.get());
      Optional<Address> existingShippingAddress = this.findAddress(pojoShippingAddress);
      if (existingShippingAddress.isEmpty()) {
        Address address = addressesConverterService.convertToNewEntity(pojoShippingAddress);
        target.setShippingAddress(address);
      } else {
        target.setShippingAddress(existingShippingAddress.get());
      }
    }
  }

  /**
   * Straightly converts the details data.
   * Uses a stream function chain, see {@link #convertDetailToNewEntity}(SellDetailPojo d).
   */
  private void convertDetailsForEntity(SellPojo model, Sell target) {
    List<SellDetail> detailEntities = model.getDetails().stream()
      .map(this::convertDetailToNewEntity)
      .collect(Collectors.toList());
    target.setDetails(detailEntities);
  }

  /**
   * Updates the net value, taxes, total units and total value of the sell.
   */
  private void updateTotals(Sell entity) {
    int netValue = 0;
    int taxesValue = 0;
    int totalUnits = 0;
    for (SellDetail sd : entity.getDetails()) {
      int unitValue = sd.getUnitValue();
      double unitTaxValue = unitValue * TAX_PERCENT;
      double unitNetValue = unitValue - unitTaxValue;
      taxesValue += (unitTaxValue * sd.getUnits());
      netValue += (unitNetValue * sd.getUnits());
      totalUnits += sd.getUnits();
    }
    entity.setTaxesValue(taxesValue);
    entity.setNetValue(netValue);
    entity.setTotalValue(taxesValue + netValue);
    entity.setTotalItems(totalUnits);
  }

  /**
   * Matches an address using all of its fields.
   */
  private Optional<Address> findAddress(@NotNull AddressPojo address) {
    return addressesRepository.findByFields(
      address.getCity(),
      address.getMunicipality(),
      address.getFirstLine(),
      address.getSecondLine(),
      address.getPostalCode(),
      address.getNotes()
    );
  }
}
