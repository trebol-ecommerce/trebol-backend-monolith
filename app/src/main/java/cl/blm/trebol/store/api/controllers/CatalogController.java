package cl.blm.trebol.store.api.controllers;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.blm.trebol.store.api.pojo.ProductFamilyPojo;
import cl.blm.trebol.store.api.pojo.ProductPojo;
import cl.blm.trebol.store.api.pojo.ProductTypePojo;
import cl.blm.trebol.store.services.CatalogService;

@RestController
@RequestMapping("/catalog")
public class CatalogController {

  private final CatalogService catalogService;

  @Autowired
  public CatalogController(CatalogService catalogService) {
    this.catalogService = catalogService;
  }

  @GetMapping("/product/{id}")
  public ProductPojo readOne(@PathVariable Integer id) {
    return catalogService.readProduct(id);
  }

  @GetMapping("/products")
  public Collection<ProductPojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    return catalogService.readProducts(10, 0, allRequestParams);
  }

  @GetMapping("/products/{requestPageSize}")
  public Collection<ProductPojo> readMany(@PathVariable Integer requestPageSize,
      @RequestParam Map<String, String> allRequestParams) {
    return catalogService.readProducts(requestPageSize, 0, allRequestParams);
  }

  @GetMapping("/products/{requestPageSize}/{requestPageIndex}")
  public Collection<ProductPojo> readMany(@PathVariable Integer requestPageSize, @PathVariable Integer requestPageIndex,
      @RequestParam Map<String, String> allRequestParams) {
    return catalogService.readProducts(requestPageSize, requestPageIndex, allRequestParams);
  }

  @GetMapping("/product_families")
  public Collection<ProductFamilyPojo> readProductFamilies() {
    return catalogService.readProductFamilies();
  }

  @GetMapping("/product_types")
  public Collection<ProductTypePojo> readProductTypes() {
    return catalogService.readProductTypes();
  }

  @GetMapping("/product_types")
  public Collection<ProductTypePojo> readProductTypes(@RequestParam("familyId") Integer familyId) {
    return catalogService.readProductTypesByFamilyId(familyId);
  }
}
