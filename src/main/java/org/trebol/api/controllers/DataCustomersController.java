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

import org.trebol.api.pojo.CustomerPojo;
import org.trebol.api.DataPage;
import org.trebol.api.GenericDataController;
import org.trebol.config.CustomProperties;
import org.trebol.jpa.entities.Customer;
import org.trebol.exceptions.EntityAlreadyExistsException;
import org.trebol.jpa.GenericJpaCrudService;

import com.querydsl.core.types.Predicate;

import org.trebol.api.IDataCrudController;
import org.trebol.exceptions.BadInputException;

import javassist.NotFoundException;

/**
 * API point of entry for Customer entities
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/data/customers")
public class DataCustomersController
  extends GenericDataController<CustomerPojo, Customer>
  implements IDataCrudController<CustomerPojo, String> {

  @Autowired
  public DataCustomersController(CustomProperties globals, GenericJpaCrudService<CustomerPojo, Customer> crudService) {
    super(globals, crudService);
  }

  @GetMapping({"", "/"})
  @PreAuthorize("hasAuthority('customers:read')")
  public DataPage<CustomerPojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    return super.readMany(null, null, allRequestParams);
  }

  @Override
  @PostMapping({"", "/"})
  @PreAuthorize("hasAuthority('customers:create')")
  public void create(@RequestBody @Valid CustomerPojo input) throws BadInputException, EntityAlreadyExistsException {
    crudService.create(input);
  }

  @Override
  @GetMapping({"/{idNumber}", "/{idNumber}/"})
  @PreAuthorize("hasAuthority('customers:read')")
  public CustomerPojo readOne(@PathVariable String idNumber) throws NotFoundException {
    Map<String, String> idNumberMatchMap = Maps.of("idnumber", idNumber).build();
    Predicate filters = crudService.parsePredicate(idNumberMatchMap);
    return crudService.readOne(filters);
  }

  @Override
  @PutMapping({"/{idNumber}", "/{idNumber}/"})
  @PreAuthorize("hasAuthority('customers:update')")
  public void update(@RequestBody @Valid CustomerPojo input, @PathVariable String idNumber)
    throws NotFoundException, BadInputException {
    // TODO improve this implementation; the same customer will be fetched twice
    Long customerId = this.readOne(idNumber).getId();
    crudService.update(input, customerId);
  }

  @Override
  @DeleteMapping({"/{idNumber}", "/{idNumber}/"})
  @PreAuthorize("hasAuthority('customers:delete')")
  public void delete(@PathVariable String idNumber) throws NotFoundException {
    Long customerId = this.readOne(idNumber).getId();
    crudService.delete(customerId);
  }

  @Override
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Map<String, String> handleException(MethodArgumentNotValidException ex) {
    return super.handleException(ex);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(EntityAlreadyExistsException.class)
  public void handleException(EntityAlreadyExistsException ex) { }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(NotFoundException.class)
  public void handleException(NotFoundException ex) { }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(BadInputException.class)
  public String handleException(BadInputException ex) {
    return ex.getMessage();
  }
}
