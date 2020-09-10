package cl.blm.newmarketing.backend.api.controllers;

import java.util.Collection;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import cl.blm.newmarketing.backend.CustomProperties;
import cl.blm.newmarketing.backend.api.GenericEntityDataController;
import cl.blm.newmarketing.backend.api.pojo.ProductFamilyPojo;
import cl.blm.newmarketing.backend.jpa.entities.ProductFamily;
import cl.blm.newmarketing.backend.services.data.GenericEntityDataService;

/**
 * API point of entry for ProductFamily entities
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/api")
public class ProductFamiliesDataController
    extends GenericEntityDataController<ProductFamilyPojo, ProductFamily, Integer> {

  @Autowired
  public ProductFamiliesDataController(CustomProperties globals,
      GenericEntityDataService<ProductFamilyPojo, ProductFamily, Integer> crudService) {
    super(globals, crudService);
  }

  @Override
  @PostMapping("/product_family")
  public Integer create(@RequestBody @Valid ProductFamilyPojo input) {
    return super.create(input);
  }

  @Override
  @GetMapping("/product_family/{id}")
  public ProductFamilyPojo readOne(@PathVariable Integer id) {
    return super.readOne(id);
  }

  @GetMapping("/product_families")
  public Collection<ProductFamilyPojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    return super.readMany(null, null, allRequestParams);
  }

  @GetMapping("/product_families/{requestPageSize}")
  public Collection<ProductFamilyPojo> readMany(@PathVariable Integer requestPageSize,
      @RequestParam Map<String, String> allRequestParams) {
    return super.readMany(requestPageSize, null, allRequestParams);
  }

  @Override
  @GetMapping("/product_families/{requestPageSize}/{requestPageIndex}")
  public Collection<ProductFamilyPojo> readMany(@PathVariable Integer requestPageSize,
      @PathVariable Integer requestPageIndex, @RequestParam Map<String, String> allRequestParams) {
    return super.readMany(requestPageSize, requestPageIndex, allRequestParams);
  }

  @PutMapping("/product_family")
  public Integer update(@RequestBody @Valid ProductFamilyPojo input) {
    return super.update(input, input.getId());
  }

  @Override
  @PutMapping("/product_family/{id}")
  public Integer update(@RequestBody @Valid ProductFamilyPojo input, @PathVariable Integer id) {
    return super.update(input, id);
  }

  @Override
  @DeleteMapping("/product_family/{id}")
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
