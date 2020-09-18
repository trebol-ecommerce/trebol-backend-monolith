package cl.blm.newmarketing.store.api.controllers.crud;

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

import cl.blm.newmarketing.store.api.GenericEntityDataController;
import cl.blm.newmarketing.store.api.pojo.ProductPojo;
import cl.blm.newmarketing.store.config.CustomProperties;
import cl.blm.newmarketing.store.jpa.entities.Product;
import cl.blm.newmarketing.store.services.crud.GenericEntityCrudService;

/**
 * API point of entry for Product entities
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/api")
public class ProductsCrudController
    extends GenericEntityDataController<ProductPojo, Product, Integer> {

  @Autowired
  public ProductsCrudController(CustomProperties globals,
      GenericEntityCrudService<ProductPojo, Product, Integer> crudService) {
    super(globals, crudService);
  }

  @Override
  @PostMapping("/product")
  public Integer create(@RequestBody @Valid ProductPojo input) {
    return super.create(input);
  }

  @Override
  @GetMapping("/product/{id}")
  public ProductPojo readOne(@PathVariable Integer id) {
    return super.readOne(id);
  }

  @GetMapping("/products")
  public Collection<ProductPojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    return super.readMany(null, null, allRequestParams);
  }

  @GetMapping("/products/{requestPageSize}")
  public Collection<ProductPojo> readMany(@PathVariable Integer requestPageSize,
      @RequestParam Map<String, String> allRequestParams) {
    return super.readMany(requestPageSize, null, allRequestParams);
  }

  @Override
  @GetMapping("/products/{requestPageSize}/{requestPageIndex}")
  public Collection<ProductPojo> readMany(@PathVariable Integer requestPageSize, @PathVariable Integer requestPageIndex,
      @RequestParam Map<String, String> allRequestParams) {
    return super.readMany(requestPageSize, requestPageIndex, allRequestParams);
  }

  @PutMapping("/product")
  public Integer update(@RequestBody @Valid ProductPojo input) {
    return super.update(input, input.getId());
  }

  @Override
  @PutMapping("/product/{id}")
  public Integer update(@RequestBody @Valid ProductPojo input, @PathVariable Integer id) {
    return super.update(input, id);
  }

  @Override
  @DeleteMapping("/product/{id}")
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
