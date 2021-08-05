package org.trebol.api.controllers;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.trebol.api.CrudController;

import org.trebol.api.DataPage;
import org.trebol.api.GenericDataController;
import org.trebol.api.pojo.ProductCategoryPojo;
import org.trebol.config.CustomProperties;
import org.trebol.jpa.entities.ProductCategory;
import org.trebol.jpa.exceptions.EntityAlreadyExistsException;
import org.trebol.jpa.services.GenericJpaCrudService;

/**
 * API point of entry for SellStatus entities
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/data/sell_statuses")
public class DataSellStatusesController
  extends GenericDataController<ProductCategoryPojo, ProductCategory>
  implements CrudController<ProductCategoryPojo, String> {

  @Autowired
  public DataSellStatusesController(CustomProperties globals,
      GenericJpaCrudService<ProductCategoryPojo, ProductCategory> crudService) {
    super(globals, crudService);
  }

  @GetMapping({"", "/"})
  @PreAuthorize("hasAuthority('sell_statuses:read')")
  public DataPage<ProductCategoryPojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    return super.readMany(null, null, allRequestParams);
  }

  @Override
  @PostMapping({"", "/"})
  @PreAuthorize("hasAuthority('sell_statuses:create')")
  public void create(
    @RequestBody @Valid ProductCategoryPojo input
  ) throws EntityAlreadyExistsException {
    crudService.create(input);
  }

  @Override
  @GetMapping({"/{code}", "/{code}/"})
  @PreAuthorize("hasAuthority('sell_statuses:read')")
  public ProductCategoryPojo readOne(@PathVariable String code) {
    throw new UnsupportedOperationException("Method not implemented");
  }

  @Override
  @PutMapping({"/{code}", "/{code}/"})
  @PreAuthorize("hasAuthority('sell_statuses:update')")
  public void update(@RequestBody @Valid ProductCategoryPojo input, @PathVariable String code) {
    throw new UnsupportedOperationException("Method not implemented");
  }

  @Override
  @DeleteMapping({"/{code}", "/{code}/"})
  @PreAuthorize("hasAuthority('sell_statuses:delete')")
  public void delete(@PathVariable String code) {
    throw new UnsupportedOperationException("Method not implemented");
  }

  @Override
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
    return super.handleValidationExceptions(ex);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(EntityAlreadyExistsException.class)
  public void handleConstraintExceptions(EntityAlreadyExistsException ex) { }
}
