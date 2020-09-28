package cl.blm.newmarketing.store.api.controllers;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.blm.newmarketing.store.api.GenericEntityDataController;
import cl.blm.newmarketing.store.api.pojo.ProductPojo;
import cl.blm.newmarketing.store.config.CustomProperties;
import cl.blm.newmarketing.store.jpa.entities.Product;
import cl.blm.newmarketing.store.services.crud.GenericEntityCrudService;

@RestController
@RequestMapping("/catalog")
public class CatalogController
    extends GenericEntityDataController<ProductPojo, Product, Integer> {

  @Autowired
  public CatalogController(CustomProperties customProperties,
      GenericEntityCrudService<ProductPojo, Product, Integer> crudService) {
    super(customProperties, crudService);
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

}
