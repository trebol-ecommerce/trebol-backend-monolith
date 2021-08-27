package org.trebol.api.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.trebol.api.DataPage;

import org.trebol.api.pojo.ProductPojo;
import org.trebol.config.CustomProperties;
import org.trebol.jpa.GenericJpaCrudService;
import org.trebol.jpa.entities.Product;

import com.querydsl.core.types.Predicate;

import javassist.NotFoundException;

@RestController
@RequestMapping("/public/products")
public class PublicProductsController {

  private final GenericJpaCrudService<ProductPojo, Product> productsCrudService;
  private final CustomProperties customProperties;

  @Autowired
  public PublicProductsController(GenericJpaCrudService<ProductPojo, Product> catalogService,
    CustomProperties customProperties) {
    this.productsCrudService = catalogService;
    this.customProperties = customProperties;
  }

  @GetMapping({"", "/"})
  public DataPage<ProductPojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    Integer requestPageSize = customProperties.getItemsPerPage();
    Predicate filters = productsCrudService.parsePredicate(allRequestParams);
    return productsCrudService.readMany(requestPageSize, 0, filters);
  }

  @GetMapping({"/{id}", "/{id}/"})
  public ProductPojo readOne(@PathVariable Long id) throws NotFoundException {
    return productsCrudService.readOne(id);
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(NotFoundException.class)
  public void handleException(NotFoundException ex) { }
}
