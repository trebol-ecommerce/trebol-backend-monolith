package cl.blm.newmarketing.backend.api.controllers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
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
import cl.blm.newmarketing.backend.api.DataServiceClient;
import cl.blm.newmarketing.backend.api.GenericDataController;
import cl.blm.newmarketing.backend.api.pojo.ProductFamilyPojo;
import cl.blm.newmarketing.backend.model.entities.ProductFamily;
import cl.blm.newmarketing.backend.services.data.GenericDataService;

/**
 * API point of entry for Sell entities
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/api")
public class ProductFamiliesDataController
    extends DataServiceClient<ProductFamilyPojo, ProductFamily, Integer>
    implements GenericDataController<ProductFamilyPojo, Integer> {
  private final static Logger LOG = LoggerFactory.getLogger(ProductFamiliesDataController.class);

  @Autowired
  public ProductFamiliesDataController(CustomProperties globals,
      GenericDataService<ProductFamilyPojo, ProductFamily, Integer> crudService) {
    super(globals, crudService);
  }

  @PostMapping("/product_family")
  public Integer create(@RequestBody @Valid ProductFamilyPojo input) {
    LOG.info("create");
    Integer resultId = crudService.create(input);
    return resultId;
  }

  @GetMapping("/product_family/{id}")
  public ProductFamilyPojo readOne(@PathVariable Integer id) {
    LOG.info("read");
    ProductFamilyPojo found = crudService.find(id);
    return found;
  }

  @GetMapping("/product_families")
  public Collection<ProductFamilyPojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    return this.readMany(null, null, allRequestParams);
  }

  @GetMapping("/product_families/{requestPageSize}")
  public Collection<ProductFamilyPojo> readMany(@PathVariable Integer requestPageSize,
      @RequestParam Map<String, String> allRequestParams) {
    return this.readMany(requestPageSize, null, allRequestParams);
  }

  @GetMapping("/product_families/{requestPageSize}/{requestPageIndex}")
  public Collection<ProductFamilyPojo> readMany(@PathVariable Integer requestPageSize,
      @PathVariable Integer requestPageIndex, @RequestParam Map<String, String> allRequestParams) {
    LOG.info("read");
    Collection<ProductFamilyPojo> sales = this.readFromService(requestPageSize, requestPageIndex, allRequestParams);
    return sales;
  }

  @PutMapping("/product_family")
  public Integer update(@RequestBody @Valid ProductFamilyPojo input) {
    LOG.info("update");
    Integer resultId = crudService.update(input, input.getId());
    return resultId;
  }

  @PutMapping("/product_family/{id}")
  public Integer update(@RequestBody @Valid ProductFamilyPojo input, @PathVariable Integer id) {
    LOG.info("update");
    Integer resultId = crudService.update(input, id);
    return resultId;
  }

  @DeleteMapping("/product_family/{id}")
  public boolean delete(@PathVariable Integer id) {
    LOG.info("delete");
    boolean result = crudService.delete(id);
    return result;
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
    LOG.info("exception handled");
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    return errors;
  }
}
