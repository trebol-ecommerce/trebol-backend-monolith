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
import cl.blm.newmarketing.backend.api.GenericDataController;
import cl.blm.newmarketing.backend.api.GenericEntityQueryController;
import cl.blm.newmarketing.backend.api.pojo.ProductPojo;
import cl.blm.newmarketing.backend.model.entities.Product;
import cl.blm.newmarketing.backend.services.data.GenericDataService;

/**
 * API point of entry for Product entities
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/api")
public class ProductsDataController
    extends GenericEntityQueryController<ProductPojo, Product, Integer>
    implements GenericDataController<ProductPojo, Integer> {
  private final static Logger LOG = LoggerFactory.getLogger(ProductsDataController.class);

  @Autowired
  public ProductsDataController(CustomProperties globals,
      GenericDataService<ProductPojo, Product, Integer> crudService) {
    super(globals, crudService);
  }

  @PostMapping("/product")
  public Integer create(@RequestBody @Valid ProductPojo input) {
    LOG.info("create");
    Integer resultId = crudService.create(input);
    return resultId;
  }

  @GetMapping("/product/{id}")
  public ProductPojo readOne(@PathVariable Integer id) {
    LOG.info("read");
    ProductPojo found = crudService.find(id);
    return found;
  }

  @GetMapping("/products")
  public Collection<ProductPojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    return this.readMany(null, null, allRequestParams);
  }

  @GetMapping("/products/{requestPageSize}")
  public Collection<ProductPojo> readMany(@PathVariable Integer requestPageSize,
      @RequestParam Map<String, String> allRequestParams) {
    return this.readMany(requestPageSize, null, allRequestParams);
  }

  @Override
  @GetMapping("/products/{requestPageSize}/{requestPageIndex}")
  public Collection<ProductPojo> readMany(@PathVariable Integer requestPageSize, @PathVariable Integer requestPageIndex,
      @RequestParam Map<String, String> allRequestParams) {
    LOG.info("read");
    Collection<ProductPojo> products = super.readMany(requestPageSize, requestPageIndex, allRequestParams);
    return products;
  }

  @PutMapping("/product")
  public Integer update(@RequestBody @Valid ProductPojo input) {
    LOG.info("update");
    Integer resultId = crudService.update(input, input.getId());
    return resultId;
  }

  @PutMapping("/product/{id}")
  public Integer update(@RequestBody @Valid ProductPojo input, @PathVariable Integer id) {
    LOG.info("update");
    Integer resultId = crudService.update(input, id);
    return resultId;
  }

  @DeleteMapping("/product/{id}")
  public boolean delete(@PathVariable Integer id) {
    LOG.info("delete");
    boolean result = crudService.delete(id);
    return result;
  }
}
