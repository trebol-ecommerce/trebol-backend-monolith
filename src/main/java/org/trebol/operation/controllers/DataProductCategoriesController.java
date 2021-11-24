package org.trebol.operation.controllers;

import java.util.Map;
import java.util.Optional;

import com.querydsl.core.types.Predicate;
import io.jsonwebtoken.lang.Maps;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.trebol.exceptions.BadInputException;
import org.trebol.exceptions.EntityAlreadyExistsException;
import org.trebol.operation.GenericDataController;
import org.trebol.operation.IDataCrudController;
import org.trebol.pojo.DataPagePojo;
import org.trebol.pojo.ImagePojo;
import org.trebol.pojo.ProductCategoryPojo;
import org.trebol.config.OperationProperties;
import org.trebol.jpa.entities.ProductCategory;
import org.trebol.jpa.GenericJpaService;

import javax.validation.Valid;

/**
 * Controller that maps API resources for CRUD operations on ProductCategories
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/data/product_categories")
public class DataProductCategoriesController
  extends GenericDataController<ProductCategoryPojo, ProductCategory>
  implements IDataCrudController<ProductCategoryPojo, Long> {

  @Autowired
  public DataProductCategoriesController(OperationProperties globals,
                                         GenericJpaService<ProductCategoryPojo, ProductCategory> crudService) {
    super(globals, crudService);
  }

  @GetMapping({"", "/"})
  public DataPagePojo<ProductCategoryPojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    if (allRequestParams.isEmpty()) {
      allRequestParams.put("parentId", null);
    }
    return super.readMany(null, null, allRequestParams);
  }

  @Override
  @PostMapping({"", "/"})
  @PreAuthorize("hasAuthority('product_categories:create')")
  public void create(@Valid @RequestBody ProductCategoryPojo input) throws BadInputException, EntityAlreadyExistsException {
    crudService.create(input);
  }

  @Override
  @PutMapping({"", "/"})
  @PreAuthorize("hasAuthority('product_categories:update')")
  public void update(@Valid @RequestBody ProductCategoryPojo input, @RequestParam Map<String, String> requestParams)
      throws BadInputException, NotFoundException {
    if (!requestParams.isEmpty()) {
      Predicate predicate = crudService.parsePredicate(requestParams);
      ProductCategoryPojo match = crudService.readOne(predicate);
      crudService.update(input, match.getId());
    } else {
      crudService.update(input);
    }
  }

  @Override
  @DeleteMapping({"", "/"})
  @PreAuthorize("hasAuthority('product_categories:delete')")
  public void delete(@RequestParam Map<String, String> requestParams) throws NotFoundException {
    Predicate predicate = crudService.parsePredicate(requestParams);
    crudService.delete(predicate);
  }

  @Deprecated
  @GetMapping({"/{parentId}", "/{parentId}/"})
  public DataPagePojo<ProductCategoryPojo> readChildren(@PathVariable Long parentId) {
    Map<String, String> queryParamsMap = Maps.of("parentId", String.valueOf(parentId)).build();
    return super.readMany(null, null, queryParamsMap);
  }
}
