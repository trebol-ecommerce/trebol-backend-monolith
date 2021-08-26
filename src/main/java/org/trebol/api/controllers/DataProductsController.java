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
import org.trebol.api.pojo.ProductPojo;
import org.trebol.config.CustomProperties;
import org.trebol.jpa.entities.Product;
import org.trebol.jpa.exceptions.EntityAlreadyExistsException;
import org.trebol.jpa.services.GenericJpaCrudService;

/**
 * API point of entry for Product entities
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/data/products")
public class DataProductsController
  extends GenericDataController<ProductPojo, Product>
  implements CrudController<ProductPojo, String> {

  @Autowired
  public DataProductsController(CustomProperties globals,
      GenericJpaCrudService<ProductPojo, Product> crudService) {
    super(globals, crudService);
  }

  @GetMapping({"", "/"})
  @PreAuthorize("hasAuthority('products:read')")
  public DataPage<ProductPojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    return super.readMany(null, null, allRequestParams);
  }

  @Override
  @PostMapping({"", "/"})
  @PreAuthorize("hasAuthority('products:create')")
  public void create(
    @RequestBody @Valid ProductPojo input
  ) throws EntityAlreadyExistsException {
    crudService.create(input);
  }

  @Override
  @GetMapping({"/{code}", "/{code}/"})
  @PreAuthorize("hasAuthority('products:read')")
  public ProductPojo readOne(@PathVariable String code) {
    throw new UnsupportedOperationException("Method not implemented");
  }

  @Override
  @PutMapping({"/{code}", "/{code}/"})
  @PreAuthorize("hasAuthority('products:update')")
  public void update(@RequestBody @Valid ProductPojo input, @PathVariable String code) {
    throw new UnsupportedOperationException("Method not implemented");
  }

  @Override
  @DeleteMapping({"/{code}", "/{code}/"})
  @PreAuthorize("hasAuthority('products:delete')")
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
