package org.trebol.api.controllers;

import java.util.Map;

import io.jsonwebtoken.lang.Maps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.trebol.api.DataPage;
import org.trebol.pojo.ProductPojo;
import org.trebol.config.CustomProperties;
import org.trebol.jpa.GenericJpaCrudService;
import org.trebol.jpa.entities.Product;

import com.querydsl.core.types.Predicate;

import javassist.NotFoundException;

@RestController
@RequestMapping("/public/products")
public class PublicProductsController {

  private final GenericJpaCrudService<ProductPojo, Product> crudService;
  private final CustomProperties customProperties;

  @Autowired
  public PublicProductsController(GenericJpaCrudService<ProductPojo, Product> crudService,
    CustomProperties customProperties) {
    this.crudService = crudService;
    this.customProperties = customProperties;
  }

  @GetMapping({"", "/"})
  public DataPage<ProductPojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    Integer requestPageSize = customProperties.getItemsPerPage();
    Predicate filters = crudService.parsePredicate(allRequestParams);
    return crudService.readMany(requestPageSize, 0, filters);
  }

  @GetMapping({"/{barcode}", "/{barcode}/"})
  public ProductPojo readOne(@PathVariable String barcode) throws NotFoundException {
    Map<String, String> barcodeMatcher = Maps.of("barcode", barcode).build();
    Predicate matchesBarcode = crudService.parsePredicate(barcodeMatcher);
    return crudService.readOne(matchesBarcode);
  }
}
