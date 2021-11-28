package org.trebol.operation.controllers;

import java.util.Map;

import javax.validation.Valid;

import io.jsonwebtoken.lang.Maps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.trebol.operation.GenericDataCrudController;
import org.trebol.pojo.CustomerPojo;
import org.trebol.pojo.DataPagePojo;
import org.trebol.operation.GenericDataController;
import org.trebol.config.OperationProperties;
import org.trebol.jpa.entities.Customer;
import org.trebol.exceptions.EntityAlreadyExistsException;
import org.trebol.jpa.GenericJpaService;

import com.querydsl.core.types.Predicate;

import org.trebol.operation.IDataCrudController;
import org.trebol.exceptions.BadInputException;

import javassist.NotFoundException;
import org.trebol.pojo.ProductCategoryPojo;

/**
 * Controller that maps API resources for CRUD operations on Customers
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/data/customers")
@PreAuthorize("isAuthenticated()")
public class DataCustomersController
  extends GenericDataCrudController<CustomerPojo, Customer> {

  @Autowired
  public DataCustomersController(OperationProperties globals,
                                 GenericJpaService<CustomerPojo, Customer> crudService) {
    super(globals, crudService);
  }

  @GetMapping({"", "/"})
  @PreAuthorize("hasAuthority('customers:read')")
  public DataPagePojo<CustomerPojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    return super.readMany(null, null, allRequestParams);
  }

  @Override
  @PostMapping({"", "/"})
  @PreAuthorize("hasAuthority('customers:create')")
  public void create(@Valid @RequestBody CustomerPojo input) throws BadInputException, EntityAlreadyExistsException {
    super.create(input);
  }

  @Override
  @PutMapping({"", "/"})
  @PreAuthorize("hasAuthority('customers:update')")
  public void update(@RequestBody CustomerPojo input, @RequestParam Map<String, String> requestParams)
      throws NotFoundException, BadInputException {
    super.update(input, requestParams);
  }

  @Override
  @DeleteMapping({"", "/"})
  @PreAuthorize("hasAuthority('customers:delete')")
  public void delete(Map<String, String> requestParams) throws NotFoundException {
    super.delete(requestParams);
  }

  @Deprecated(forRemoval = true)
  @GetMapping({"/{idNumber}", "/{idNumber}/"})
  @PreAuthorize("hasAuthority('customers:read')")
  public CustomerPojo readOne(@PathVariable String idNumber) throws NotFoundException {
    Map<String, String> idNumberMatchMap = Maps.of("idnumber", idNumber).build();
    Predicate filters = crudService.parsePredicate(idNumberMatchMap);
    return crudService.readOne(filters);
  }

  @Deprecated(forRemoval = true)
  @PutMapping({"/{idNumber}", "/{idNumber}/"})
  @PreAuthorize("hasAuthority('customers:update')")
  public void update(@RequestBody CustomerPojo input, @PathVariable String idNumber)
    throws NotFoundException, BadInputException {
    Long customerId = this.readOne(idNumber).getId();
    crudService.update(input, customerId);
  }

  @Deprecated(forRemoval = true)
  @DeleteMapping({"/{idNumber}", "/{idNumber}/"})
  @PreAuthorize("hasAuthority('customers:delete')")
  public void delete(@PathVariable String idNumber) throws NotFoundException {
    Long customerId = this.readOne(idNumber).getId();
    crudService.delete(customerId);
  }
}
