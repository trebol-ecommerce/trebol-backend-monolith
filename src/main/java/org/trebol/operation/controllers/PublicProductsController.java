package org.trebol.operation.controllers;

import com.querydsl.core.types.Predicate;
import io.jsonwebtoken.lang.Maps;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.trebol.config.OperationProperties;
import org.trebol.jpa.entities.Product;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.IPredicateJpaService;
import org.trebol.pojo.DataPagePojo;
import org.trebol.pojo.ProductPojo;

import java.util.Map;

@RestController
@RequestMapping("/public/products")
public class PublicProductsController {

  private final GenericCrudJpaService<ProductPojo, Product> crudService;
  private final IPredicateJpaService<Product> predicateService;
  private final OperationProperties operationProperties;

  @Autowired
  public PublicProductsController(GenericCrudJpaService<ProductPojo, Product> crudService,
                                  IPredicateJpaService<Product> predicateService,
                                  OperationProperties operationProperties) {
    this.crudService = crudService;
    this.predicateService = predicateService;
    this.operationProperties = operationProperties;
  }

  @Deprecated(forRemoval = true)
  @GetMapping({"", "/"})
  public DataPagePojo<ProductPojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    int requestPageSize = this.determineRequestedPageSize(allRequestParams);
    Predicate filters = predicateService.parseMap(allRequestParams);
    return crudService.readMany(0, requestPageSize, null, filters);
  }

  @Deprecated(forRemoval = true)
  @GetMapping({"/{barcode}", "/{barcode}/"})
  public ProductPojo readOne(@PathVariable String barcode) throws NotFoundException {
    Map<String, String> barcodeMatcher = Maps.of("barcode", barcode).build();
    Predicate matchesBarcode = predicateService.parseMap(barcodeMatcher);
    return crudService.readOne(matchesBarcode);
  }

  private int determineRequestedPageSize(Map<String, String> allRequestParams)
      throws NumberFormatException {
    if (allRequestParams != null && allRequestParams.containsKey("pageSize")) {
      int pageSize = Integer.parseInt(allRequestParams.get("pageSize"));
      if (pageSize > 0) {
        return pageSize;
      }
    }
    return operationProperties.getItemsPerPage();
  }
}
