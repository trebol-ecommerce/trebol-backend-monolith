package org.trebol.api.controllers.data;

import java.util.Collection;
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
import org.trebol.api.pojo.ProductFamilyPojo;
import org.trebol.config.CustomProperties;
import org.trebol.jpa.entities.ProductFamily;
import org.trebol.services.crud.GenericCrudService;

/**
 * API point of entry for ProductFamily entities
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/data/product_families")
public class ProductFamiliesDataController
    extends GenericCrudController<ProductFamilyPojo, ProductFamily, Integer> {

  @Autowired
  public ProductFamiliesDataController(CustomProperties globals,
      GenericCrudService<ProductFamilyPojo, ProductFamily, Integer> crudService) {
    super(globals, crudService);
  }

  @GetMapping
  @PreAuthorize("hasAuthority('product_families:read')")
  public Collection<ProductFamilyPojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    return super.readMany(null, null, allRequestParams);
  }

  @Override
  @PostMapping
  @PreAuthorize("hasAuthority('product_families:create')")
  public Integer create(@RequestBody @Valid ProductFamilyPojo input) {
    return super.create(input);
  }

  @Override
  @GetMapping("/{id}")
  @PreAuthorize("hasAuthority('product_families:read')")
  public ProductFamilyPojo readOne(@PathVariable Integer id) {
    return super.readOne(id);
  }

  @Override
  @PutMapping("/{id}")
  @PreAuthorize("hasAuthority('product_families:update')")
  public Integer update(@RequestBody @Valid ProductFamilyPojo input, @PathVariable Integer id) {
    return super.update(input, id);
  }

  @Override
  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('product_families:delete')")
  public boolean delete(@PathVariable Integer id) {
    return super.delete(id);
  }

  @Override
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
    return super.handleValidationExceptions(ex);
  }
}
