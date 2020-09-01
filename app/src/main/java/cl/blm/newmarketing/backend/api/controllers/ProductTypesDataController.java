package cl.blm.newmarketing.backend.api.controllers;

import java.util.Collection;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.blm.newmarketing.backend.CustomProperties;
import cl.blm.newmarketing.backend.api.DataServiceClient;
import cl.blm.newmarketing.backend.api.GenericDataController;
import cl.blm.newmarketing.backend.api.pojo.ProductTypePojo;
import cl.blm.newmarketing.backend.model.entities.ProductType;
import cl.blm.newmarketing.backend.services.data.GenericDataService;

/**
 * API point of entry for Sell entities
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/api")
public class ProductTypesDataController
    extends DataServiceClient<ProductTypePojo, ProductType, Integer>
    implements GenericDataController<ProductTypePojo, Integer> {
  private final static Logger LOG = LoggerFactory.getLogger(ProductTypesDataController.class);

  @Autowired
  public ProductTypesDataController(CustomProperties globals,
      GenericDataService<ProductTypePojo, ProductType, Integer> crudService) {
    super(globals, crudService);
  }

  @PostMapping("/product_type")
  public ProductTypePojo create(@RequestBody @Valid ProductTypePojo input) {
    LOG.info("create");
    ProductTypePojo result = crudService.create(input);
    return result;
  }

  @GetMapping("/product_type/{id}")
  public ProductTypePojo readOne(@PathVariable Integer id) {
    LOG.info("read");
    ProductTypePojo found = crudService.find(id);
    return found;
  }

  @GetMapping("/product_types")
  public Collection<ProductTypePojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    return this.readMany(null, null, allRequestParams);
  }

  @GetMapping("/product_types/{requestPageSize}")
  public Collection<ProductTypePojo> readMany(@PathVariable Integer requestPageSize,
      @RequestParam Map<String, String> allRequestParams) {
    return this.readMany(requestPageSize, null, allRequestParams);
  }

  @GetMapping("/product_types/{requestPageSize}/{requestPageIndex}")
  public Collection<ProductTypePojo> readMany(@PathVariable Integer requestPageSize,
      @PathVariable Integer requestPageIndex, @RequestParam Map<String, String> allRequestParams) {
    LOG.info("read");
    Collection<ProductTypePojo> sales = this.readFromService(requestPageSize, requestPageIndex, allRequestParams);
    return sales;
  }

  @PutMapping("/product_type")
  public ProductTypePojo update(@RequestBody @Valid ProductTypePojo input) {
    LOG.info("update");
    ProductTypePojo result = crudService.update(input, input.getId());
    return result;
  }

  @PutMapping("/product_type/{id}")
  public ProductTypePojo update(@RequestBody @Valid ProductTypePojo input, @PathVariable Integer id) {
    LOG.info("update");
    ProductTypePojo result = crudService.update(input, id);
    return result;
  }

  @DeleteMapping("/product_type/{id}")
  public boolean delete(@PathVariable Integer id) {
    LOG.info("delete");
    boolean result = crudService.delete(id);
    return result;
  }
}
