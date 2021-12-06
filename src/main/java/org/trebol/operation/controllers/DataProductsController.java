package org.trebol.operation.controllers;

import com.querydsl.core.types.Predicate;
import io.jsonwebtoken.lang.Maps;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.trebol.config.OperationProperties;
import org.trebol.exceptions.BadInputException;
import org.trebol.exceptions.EntityAlreadyExistsException;
import org.trebol.jpa.entities.Product;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.IPredicateJpaService;
import org.trebol.operation.GenericDataCrudController;
import org.trebol.pojo.DataPagePojo;
import org.trebol.pojo.ProductPojo;

import javax.validation.Valid;
import java.util.Map;

/**
 * Controller that maps API resources for CRUD operations on Products
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/data/products")
@PreAuthorize("isAuthenticated()")
public class DataProductsController
  extends GenericDataCrudController<ProductPojo, Product> {

  @Autowired
  public DataProductsController(OperationProperties globals,
                                GenericCrudJpaService<ProductPojo, Product> crudService,
                                IPredicateJpaService<Product> predicateService) {
    super(globals, crudService, predicateService);
  }

  @GetMapping({"", "/"})
  @PreAuthorize("hasAuthority('products:read')")
  public DataPagePojo<ProductPojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    return super.readMany(null, null, allRequestParams);
  }

  @Override
  @PostMapping({"", "/"})
  @PreAuthorize("hasAuthority('products:create')")
  public void create(@Valid @RequestBody ProductPojo input) throws BadInputException, EntityAlreadyExistsException {
    super.create(input);
  }

  @Override
  @PutMapping({"", "/"})
  @PreAuthorize("hasAuthority('products:update')")
  public void update(@RequestBody ProductPojo input, @RequestParam Map<String, String> requestParams)
      throws BadInputException, NotFoundException {
    super.update(input, requestParams);
  }

  @Override
  @DeleteMapping({"", "/"})
  @PreAuthorize("hasAuthority('products:delete')")
  public void delete(@RequestParam Map<String, String> requestParams) throws NotFoundException {
    super.delete(requestParams);
  }

  @Deprecated(forRemoval = true)
  @GetMapping({"/{barcode}", "/{barcode}/"})
  @PreAuthorize("hasAuthority('products:read')")
  public ProductPojo readOne(@PathVariable String barcode) throws NotFoundException {
    return crudService.readOne(whereBarcodeIs(barcode));
  }

  @Deprecated(forRemoval = true)
  @PutMapping({"/{barcode}", "/{barcode}/"})
  @PreAuthorize("hasAuthority('products:update')")
  public void update(@RequestBody ProductPojo input, @PathVariable String barcode)
    throws BadInputException, NotFoundException {
    crudService.update(input, whereBarcodeIs(barcode));
  }

  @Deprecated(forRemoval = true)
  @DeleteMapping({"/{barcode}", "/{barcode}/"})
  @PreAuthorize("hasAuthority('products:delete')")
  public void delete(@PathVariable String barcode) throws NotFoundException {
    crudService.delete(whereBarcodeIs(barcode));
  }

  private Predicate whereBarcodeIs(String barcode) {
    Map<String, String> barcodeMatcher = Maps.of("barcode", barcode).build();
    return predicateService.parseMap(barcodeMatcher);
  }
}
