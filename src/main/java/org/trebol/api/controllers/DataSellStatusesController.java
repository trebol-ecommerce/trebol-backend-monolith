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

import org.trebol.api.GenericCrudController;
import org.trebol.api.DataPage;
import org.trebol.api.pojo.ProductCategoryPojo;
import org.trebol.config.CustomProperties;
import org.trebol.jpa.entities.ProductCategory;
import org.trebol.jpa.services.GenericCrudService;

/**
 * API point of entry for SellStatus entities
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/data/sell_statuses")
public class DataSellStatusesController
    extends GenericCrudController<ProductCategoryPojo, ProductCategory, Integer> {

  @Autowired
  public DataSellStatusesController(CustomProperties globals,
      GenericCrudService<ProductCategoryPojo, ProductCategory, Integer> crudService) {
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
  public void create(@RequestBody @Valid ProductCategoryPojo input) {
    super.create(input);
  }

  @Override
  @GetMapping({"/{id}", "/{id}/"})
  @PreAuthorize("hasAuthority('sell_statuses:read')")
  public ProductCategoryPojo readOne(@PathVariable Integer id) {
    return super.readOne(id);
  }

  @Override
  @PutMapping({"/{id}", "/{id}/"})
  @PreAuthorize("hasAuthority('sell_statuses:update')")
  public void update(@RequestBody @Valid ProductCategoryPojo input, @PathVariable Integer id) {
    super.update(input, id);
  }

  @Override
  @DeleteMapping({"/{id}", "/{id}/"})
  @PreAuthorize("hasAuthority('sell_statuses:delete')")
  public void delete(@PathVariable Integer id) {
    super.delete(id);
  }

  @Override
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
    return super.handleValidationExceptions(ex);
  }
}
