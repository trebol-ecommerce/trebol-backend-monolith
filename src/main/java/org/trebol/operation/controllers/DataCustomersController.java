package org.trebol.operation.controllers;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.trebol.config.OperationProperties;
import org.trebol.exceptions.BadInputException;
import org.trebol.exceptions.EntityAlreadyExistsException;
import org.trebol.jpa.entities.Customer;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.IPredicateJpaService;
import org.trebol.jpa.services.ISortJpaService;
import org.trebol.operation.GenericDataCrudController;
import org.trebol.pojo.CustomerPojo;
import org.trebol.pojo.DataPagePojo;

import javax.validation.Valid;
import java.util.Map;

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

  private final ISortJpaService<Customer> sortService;

  @Autowired
  public DataCustomersController(OperationProperties globals,
                                 GenericCrudJpaService<CustomerPojo, Customer> crudService,
                                 IPredicateJpaService<Customer> predicateService,
                                 ISortJpaService<Customer> sortService) {
    super(globals, crudService, predicateService);
    this.sortService = sortService;
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

  @Override
  protected Sort determineSortOrder(Map<String, String> requestParams) {
    return this.sortService.parseMap(requestParams);
  }
}
