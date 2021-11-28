package org.trebol.operation.controllers;

import java.util.Map;

import io.jsonwebtoken.lang.Maps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.trebol.pojo.DataPagePojo;
import org.trebol.pojo.ProductPojo;
import org.trebol.config.OperationProperties;
import org.trebol.jpa.GenericJpaService;
import org.trebol.jpa.entities.Product;

import com.querydsl.core.types.Predicate;

import javassist.NotFoundException;

@RestController
@RequestMapping("/public/products")
public class PublicProductsController {

  private final GenericJpaService<ProductPojo, Product> crudService;
  private final OperationProperties operationProperties;

  @Autowired
  public PublicProductsController(GenericJpaService<ProductPojo, Product> crudService,
                                  OperationProperties operationProperties) {
    this.crudService = crudService;
    this.operationProperties = operationProperties;
  }

  @GetMapping({"", "/"})
  public DataPagePojo<ProductPojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    // TODO copied from GenericDataController, must refactor into a separate service
    int requestPageSize = this.determineRequestedPageSize(allRequestParams);
    Predicate filters = crudService.parsePredicate(allRequestParams);
    return crudService.readMany(requestPageSize, 0, filters);
  }

  @GetMapping({"/{barcode}", "/{barcode}/"})
  public ProductPojo readOne(@PathVariable String barcode) throws NotFoundException {
    Map<String, String> barcodeMatcher = Maps.of("barcode", barcode).build();
    Predicate matchesBarcode = crudService.parsePredicate(barcodeMatcher);
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
