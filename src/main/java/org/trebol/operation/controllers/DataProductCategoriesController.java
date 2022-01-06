package org.trebol.operation.controllers;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.trebol.config.OperationProperties;
import org.trebol.exceptions.BadInputException;
import org.trebol.exceptions.EntityAlreadyExistsException;
import org.trebol.jpa.entities.ProductCategory;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.IPredicateJpaService;
import org.trebol.operation.GenericDataCrudController;
import org.trebol.pojo.DataPagePojo;
import org.trebol.pojo.ProductCategoryPojo;

import javax.validation.Valid;
import java.util.Map;

/**
 * Controller that maps API resources for CRUD operations on ProductCategories
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/data/product_categories")
public class DataProductCategoriesController
  extends GenericDataCrudController<ProductCategoryPojo, ProductCategory> {

  @Autowired
  public DataProductCategoriesController(OperationProperties globals,
                                         GenericCrudJpaService<ProductCategoryPojo, ProductCategory> crudService,
                                         IPredicateJpaService<ProductCategory> predicateService) {
    super(globals, crudService, predicateService);
  }

  @GetMapping({"", "/"})
  public DataPagePojo<ProductCategoryPojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    if (allRequestParams.isEmpty()) {
      allRequestParams.put("parentId", null);
    }
    return super.readMany(allRequestParams);
  }

  @Override
  @PostMapping({"", "/"})
  @PreAuthorize("hasAuthority('product_categories:create')")
  public void create(@Valid @RequestBody ProductCategoryPojo input) throws BadInputException, EntityAlreadyExistsException {
    super.create(input);
  }

  @Override
  @PutMapping({"", "/"})
  @PreAuthorize("hasAuthority('product_categories:update')")
  public void update(@Valid @RequestBody ProductCategoryPojo input, @RequestParam Map<String, String> requestParams)
      throws BadInputException, NotFoundException {
    super.update(input, requestParams);
  }

  @Override
  @DeleteMapping({"", "/"})
  @PreAuthorize("hasAuthority('product_categories:delete')")
  public void delete(@RequestParam Map<String, String> requestParams) throws NotFoundException {
    super.delete(requestParams);
  }
}
