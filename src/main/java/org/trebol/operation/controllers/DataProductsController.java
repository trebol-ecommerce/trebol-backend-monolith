package org.trebol.operation.controllers;

import java.util.Map;

import javax.validation.Valid;

import io.jsonwebtoken.lang.Maps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.trebol.pojo.DataPagePojo;
import org.trebol.operation.GenericDataController;
import org.trebol.pojo.ImagePojo;
import org.trebol.pojo.ProductPojo;
import org.trebol.config.OperationProperties;
import org.trebol.jpa.entities.Product;
import org.trebol.exceptions.EntityAlreadyExistsException;
import org.trebol.jpa.GenericJpaService;
import org.trebol.operation.IDataCrudController;
import org.trebol.exceptions.BadInputException;

import com.querydsl.core.types.Predicate;

import javassist.NotFoundException;

/**
 * Controller that maps API resources for CRUD operations on Products
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/data/products")
@PreAuthorize("isAuthenticated()")
public class DataProductsController
  extends GenericDataController<ProductPojo, Product>
  implements IDataCrudController<ProductPojo> {

  @Autowired
  public DataProductsController(OperationProperties globals, GenericJpaService<ProductPojo, Product> crudService) {
    super(globals, crudService);
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
    crudService.create(input);
  }

  @Override
  @PutMapping({"", "/"})
  @PreAuthorize("hasAuthority('products:update')")
  public void update(@RequestBody ProductPojo input, @RequestParam Map<String, String> requestParams)
      throws BadInputException, NotFoundException {
    if (!requestParams.isEmpty()) {
      Predicate predicate = crudService.parsePredicate(requestParams);
      ProductPojo existing = crudService.readOne(predicate);
      crudService.update(input, existing.getId());
    } else {
      crudService.update(input);
    }
  }

  @Override
  @DeleteMapping({"", "/"})
  @PreAuthorize("hasAuthority('products:delete')")
  public void delete(@RequestParam Map<String, String> requestParams) throws NotFoundException {
    Predicate predicate = crudService.parsePredicate(requestParams);
    crudService.delete(predicate);
  }

  @Deprecated
  @GetMapping({"/{barcode}", "/{barcode}/"})
  @PreAuthorize("hasAuthority('products:read')")
  public ProductPojo readOne(@PathVariable String barcode) throws NotFoundException {
    Map<String, String> codeMatcher = Maps.of("barcode", barcode).build();
    Predicate matchesCode = crudService.parsePredicate(codeMatcher);
    return crudService.readOne(matchesCode);
  }

  @Deprecated
  @PutMapping({"/{barcode}", "/{barcode}/"})
  @PreAuthorize("hasAuthority('products:update')")
  public void update(@RequestBody ProductPojo input, @PathVariable String barcode)
    throws BadInputException, NotFoundException {
    Long productId = this.readOne(barcode).getId();
    crudService.update(input, productId);
  }

  @Deprecated
  @DeleteMapping({"/{barcode}", "/{barcode}/"})
  @PreAuthorize("hasAuthority('products:delete')")
  public void delete(@PathVariable String barcode) throws NotFoundException {
    Long productId = this.readOne(barcode).getId();
    crudService.delete(productId);
  }
}
