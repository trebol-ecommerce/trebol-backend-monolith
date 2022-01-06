package org.trebol.operation.controllers;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.trebol.config.OperationProperties;
import org.trebol.exceptions.BadInputException;
import org.trebol.exceptions.EntityAlreadyExistsException;
import org.trebol.jpa.entities.ProductList;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.IPredicateJpaService;
import org.trebol.operation.GenericDataCrudController;
import org.trebol.pojo.DataPagePojo;
import org.trebol.pojo.ProductListPojo;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/data/product_lists")
public class DataProductListsController
  extends GenericDataCrudController<ProductListPojo, ProductList> {

  @Autowired
  public DataProductListsController(OperationProperties globals,
                                    GenericCrudJpaService<ProductListPojo, ProductList> crudService,
                                    IPredicateJpaService<ProductList> predicateService) {
    super(globals, crudService, predicateService);
  }

  @GetMapping({"", "/"})
  public DataPagePojo<ProductListPojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    return super.readMany(null, null, allRequestParams);
  }

  @Override
  @PostMapping({"", "/"})
  @PreAuthorize("hasAuthority('product_lists:create')")
  public void create(@Valid @RequestBody ProductListPojo input) throws BadInputException, EntityAlreadyExistsException {
    super.create(input);
  }

  @Override
  @PutMapping({"", "/"})
  @PreAuthorize("hasAuthority('product_lists:update')")
  public void update(@RequestBody ProductListPojo input, @RequestParam Map<String, String> requestParams)
      throws BadInputException, NotFoundException {
    super.update(input, requestParams);
  }

  @Override
  @DeleteMapping({"", "/"})
  @PreAuthorize("hasAuthority('product_lists:delete')")
  public void delete(@RequestParam Map<String, String> requestParams) throws NotFoundException {
    super.delete(requestParams);
  }
}
