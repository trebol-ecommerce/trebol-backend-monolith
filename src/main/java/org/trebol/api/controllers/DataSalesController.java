package org.trebol.api.controllers;

import java.util.Map;

import javax.validation.Valid;

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
import org.trebol.api.CrudController;

import org.trebol.api.GenericDataController;
import org.trebol.api.DataPage;
import org.trebol.api.pojo.SellPojo;
import org.trebol.config.CustomProperties;
import org.trebol.jpa.entities.Sell;
import org.trebol.jpa.exceptions.EntityAlreadyExistsException;
import org.trebol.jpa.services.GenericJpaCrudService;

import javassist.NotFoundException;

/**
 * API point of entry for Sell entities
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/data/sales")
public class DataSalesController
  extends GenericDataController<SellPojo, Sell>
  implements CrudController<SellPojo, Long> {

  @Autowired
  public DataSalesController(CustomProperties globals, GenericJpaCrudService<SellPojo, Sell> crudService) {
    super(globals, crudService);
  }

  @GetMapping({"", "/"})
  @PreAuthorize("hasAuthority('sales:read')")
  public DataPage<SellPojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    return super.readMany(null, null, allRequestParams);
  }

  @Override
  @PostMapping({"", "/"})
  @PreAuthorize("hasAuthority('sales:create')")
  public void create(@RequestBody @Valid SellPojo input) throws EntityAlreadyExistsException {
    crudService.create(input);
  }

  @Override
  @GetMapping({"/{buyOrder}", "/{buyOrder}/"})
  @PreAuthorize("hasAuthority('sales:read')")
  public SellPojo readOne(@PathVariable Long buyOrder) throws NotFoundException {
    return crudService.readOne(buyOrder);
  }

  @Override
  @PutMapping({"/{buyOrder}", "/{buyOrder}/"})
  @PreAuthorize("hasAuthority('sales:update')")
  public void update(@RequestBody @Valid SellPojo input, @PathVariable Long buyOrder) throws NotFoundException {
    crudService.update(input, buyOrder);
  }

  @Override
  @DeleteMapping({"/{buyOrder}", "/{buyOrder}/"})
  @PreAuthorize("hasAuthority('sales:delete')")
  public void delete(@PathVariable Long buyOrder) throws NotFoundException {
    crudService.delete(buyOrder);
  }

  @Override
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
    return super.handleValidationExceptions(ex);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(EntityAlreadyExistsException.class)
  public void handleConstraintExceptions(EntityAlreadyExistsException ex) { }
}
