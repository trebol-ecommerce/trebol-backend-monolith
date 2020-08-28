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
import cl.blm.newmarketing.backend.api.ApiCrudController;
import cl.blm.newmarketing.backend.api.DtoCrudServiceClient;
import cl.blm.newmarketing.backend.model.entities.Product;
import cl.blm.newmarketing.backend.services.impl.GenericCrudService;

/**
 * API point of entry for Product entities
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/api")
public class ProductsApiController
    extends DtoCrudServiceClient<Product, Integer>
    implements ApiCrudController<Product, Integer> {
  private final static Logger LOG = LoggerFactory.getLogger(ProductsApiController.class);

  @Autowired
  public ProductsApiController(CustomProperties globals, GenericCrudService<Product, Integer> crudService) {
    super(globals, crudService);
  }

  @PostMapping("/product")
  public Product create(@RequestBody @Valid Product input) {
    LOG.info("create");
    Product result = crudService.create(input);
    return result;
  }

  @GetMapping("/product/{id}")
  public Product readOne(@PathVariable Integer id) {
    LOG.info("read");
    Product found = crudService.find(id);
    return found;
  }

  @GetMapping("/products")
  public Collection<Product> readMany(@RequestParam Map<String, String> allRequestParams) {
    return this.readMany(null, null, allRequestParams);
  }

  @GetMapping("/products/{requestPageSize}")
  public Collection<Product> readMany(@PathVariable Integer requestPageSize,
      @RequestParam Map<String, String> allRequestParams) {
    return this.readMany(requestPageSize, null, allRequestParams);
  }

  @GetMapping("/products/{requestPageSize}/{requestPageIndex}")
  public Collection<Product> readMany(@PathVariable Integer requestPageSize, @PathVariable Integer requestPageIndex,
      @RequestParam Map<String, String> allRequestParams) {
    LOG.info("read");
    Collection<Product> products = this.readFromService(requestPageSize, requestPageIndex, allRequestParams);
    return products;
  }

  @PutMapping("/product")
  public Product update(@RequestBody @Valid Product input) {
    LOG.info("update");
    Product result = crudService.update(input, input.getId());
    return result;
  }

  @PutMapping("/product/{id}")
  public Product update(@RequestBody @Valid Product input, @PathVariable Integer id) {
    LOG.info("update");
    Product result = crudService.update(input, id);
    return result;
  }

  @DeleteMapping("/product/{id}")
  public boolean delete(@PathVariable Integer id) {
    LOG.info("delete");
    boolean result = crudService.delete(id);
    return result;
  }
}
