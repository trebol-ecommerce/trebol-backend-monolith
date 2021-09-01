package org.trebol.api.controllers;

import java.util.Map;

import javax.validation.Valid;

import io.jsonwebtoken.lang.Maps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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

import org.trebol.api.DataPage;
import org.trebol.api.GenericDataController;
import org.trebol.api.pojo.ProductCategoryPojo;
import org.trebol.config.CustomProperties;
import org.trebol.jpa.entities.ProductCategory;
import org.trebol.exceptions.EntityAlreadyExistsException;
import org.trebol.jpa.GenericJpaCrudService;
import org.trebol.api.IDataCrudController;
import org.trebol.exceptions.BadInputException;

import com.querydsl.core.types.Predicate;

import javassist.NotFoundException;

/**
 * API point of entry for SellStatus entities
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/data/sell_statuses")
public class DataSellStatusesController
  extends GenericDataController<ProductCategoryPojo, ProductCategory>
  implements IDataCrudController<ProductCategoryPojo, String> {

  @Autowired
  public DataSellStatusesController(CustomProperties globals,
      GenericJpaCrudService<ProductCategoryPojo, ProductCategory> crudService) {
    super(globals, crudService);
  }

  @GetMapping({"", "/"})
  @PreAuthorize("hasAuthority('sell_statuses:read')")
  public DataPage<ProductCategoryPojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    return super.readMany(null, null, allRequestParams);
  }

  @Override
  @PostMapping({"", "/"})
  @PreAuthorize("hasAuthority('sell_statuses:create')")
  public void create(@RequestBody @Valid ProductCategoryPojo input)
    throws BadInputException, EntityAlreadyExistsException {
    crudService.create(input);
  }

  @Override
  @GetMapping({"/{code}", "/{code}/"})
  @PreAuthorize("hasAuthority('sell_statuses:read')")
  public ProductCategoryPojo readOne(@PathVariable String code) throws NotFoundException {
    Map<String, String> codeMatcher = Maps.of("code", code).build();
    Predicate matchesCode = crudService.parsePredicate(codeMatcher);
    return crudService.readOne(matchesCode);
  }

  @Override
  @PutMapping({"/{code}", "/{code}/"})
  @PreAuthorize("hasAuthority('sell_statuses:update')")
  public void update(@RequestBody @Valid ProductCategoryPojo input, @PathVariable String code)
    throws BadInputException, NotFoundException {
    Long statusId = this.readOne(code).getId();
    crudService.update(input, statusId);
  }

  @Override
  @DeleteMapping({"/{code}", "/{code}/"})
  @PreAuthorize("hasAuthority('sell_statuses:delete')")
  public void delete(@PathVariable String code) throws NotFoundException {
    Long productId = this.readOne(code).getId();
    crudService.delete(productId);
  }

  @Override
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Map<String, String> handleException(MethodArgumentNotValidException ex) {
    return super.handleException(ex);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(EntityAlreadyExistsException.class)
  public String handleException(EntityAlreadyExistsException ex) {
    return ex.getMessage();
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(NotFoundException.class)
  public void handleException(NotFoundException ex) { }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(BadInputException.class)
  public String handleException(BadInputException ex) {
    return ex.getMessage();
  }
}
