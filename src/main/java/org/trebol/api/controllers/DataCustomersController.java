package org.trebol.api.controllers;

import java.util.Collection;
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
import org.trebol.api.CrudController;

import org.trebol.api.pojo.CustomerPojo;
import org.trebol.api.DataPage;
import org.trebol.api.GenericDataController;
import org.trebol.config.CustomProperties;
import org.trebol.jpa.entities.Customer;
import org.trebol.jpa.exceptions.EntityAlreadyExistsException;
import org.trebol.jpa.services.GenericJpaCrudService;

import com.querydsl.core.types.Predicate;

/**
 * API point of entry for Customer entities
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/data/customers")
public class DataCustomersController
  extends GenericDataController<CustomerPojo, Customer>
  implements CrudController<CustomerPojo, String> {

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
  public void create(
    @RequestBody @Valid CustomerPojo input
  ) throws EntityAlreadyExistsException {
    crudService.create(input);
  }

  @Override
  @GetMapping({"/{idCard}", "/{idCard}/"})
  @PreAuthorize("hasAuthority('customers:read')")
  public CustomerPojo readOne(@PathVariable String idCard) {
    Map<String, String> idCardMatchMap = Maps.of("idnumber", idCard).build();
    Predicate filters = crudService.queryParamsMapToPredicate(idCardMatchMap);
    return crudService.readOne(filters);
  }

  @Override
  @PutMapping({"/{idCard}", "/{idCard}/"})
  @PreAuthorize("hasAuthority('customers:update')")
  public void update(@RequestBody @Valid CustomerPojo input, @PathVariable String idCard) {
    // TODO improve this implementation; the same customer will be fetched twice
    Long customerId = this.readOne(idCard).getId();
    crudService.update(input, customerId);
  }

  @Override
  @DeleteMapping({"/{idCard}", "/{idCard}/"})
  @PreAuthorize("hasAuthority('customers:delete')")
  public void delete(@PathVariable String idCard) {
    Long customerId = this.readOne(idCard).getId();
    crudService.delete(customerId);
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
