package org.trebol.operation.controllers;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.trebol.config.OperationProperties;
import org.trebol.exceptions.BadInputException;
import org.trebol.exceptions.EntityAlreadyExistsException;
import org.trebol.jpa.entities.Product;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.IPredicateJpaService;
import org.trebol.jpa.services.ISortJpaService;
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

  private final ISortJpaService<Product> sortService;

  @Autowired
  public DataProductsController(OperationProperties globals,
                                GenericCrudJpaService<ProductPojo, Product> crudService,
                                IPredicateJpaService<Product> predicateService,
                                ISortJpaService<Product> sortService) {
    super(globals, crudService, predicateService);
    this.sortService = sortService;
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

  @Override
  protected Sort determineSortOrder(Map<String, String> requestParams) {
    return sortService.parseMap(requestParams);
  }
}
