package org.trebol.operation.controllers;

import com.querydsl.core.types.Predicate;
import io.jsonwebtoken.lang.Maps;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.trebol.config.OperationProperties;
import org.trebol.exceptions.BadInputException;
import org.trebol.exceptions.EntityAlreadyExistsException;
import org.trebol.jpa.entities.Salesperson;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.IPredicateJpaService;
import org.trebol.operation.GenericDataCrudController;
import org.trebol.pojo.DataPagePojo;
import org.trebol.pojo.SalespersonPojo;

import javax.validation.Valid;
import java.util.Map;

/**
 * Controller that maps API resources for CRUD operations on Salespeople
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/data/salespeople")
@PreAuthorize("isAuthenticated()")
public class DataSalespeopleController
  extends GenericDataCrudController<SalespersonPojo, Salesperson> {

  @Autowired
  public DataSalespeopleController(OperationProperties globals,
                                   GenericCrudJpaService<SalespersonPojo, Salesperson> crudService,
                                   IPredicateJpaService<Salesperson> predicateService) {
    super(globals, crudService, predicateService);
  }

  @GetMapping({"", "/"})
  @PreAuthorize("hasAuthority('salespeople:read')")
  public DataPagePojo<SalespersonPojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    return super.readMany(null, null, allRequestParams);
  }

  @Override
  @PostMapping({"", "/"})
  @PreAuthorize("hasAuthority('salespeople:create')")
  public void create(@Valid @RequestBody SalespersonPojo input) throws BadInputException, EntityAlreadyExistsException {
    super.create(input);
  }

  @Override
  @PutMapping({"", "/"})
  @PreAuthorize("hasAuthority('salespeople:update')")
  public void update(@RequestBody SalespersonPojo input, @RequestParam Map<String, String> requestParams)
      throws BadInputException, NotFoundException {
    super.update(input, requestParams);
  }

  @Override
  @DeleteMapping({"", "/"})
  @PreAuthorize("hasAuthority('salespeople:delete')")
  public void delete(@RequestParam Map<String, String> requestParams) throws NotFoundException {
    super.delete(requestParams);
  }

  @Deprecated(forRemoval = true)
  @GetMapping({"/{idNumber}", "/{idNumber}/"})
  @PreAuthorize("hasAuthority('salespeople:read')")
  public SalespersonPojo readOne(@PathVariable String idNumber) throws NotFoundException {
    return crudService.readOne(whereIdNumberIs(idNumber));
  }

  @Deprecated(forRemoval = true)
  @PutMapping({"/{idNumber}", "/{idNumber}/"})
  @PreAuthorize("hasAuthority('salespeople:update')")
  public void update(@RequestBody SalespersonPojo input, @PathVariable String idNumber)
    throws BadInputException, NotFoundException {
    crudService.update(input, whereIdNumberIs(idNumber));
  }

  @Deprecated(forRemoval = true)
  @DeleteMapping({"/{idNumber}", "/{idNumber}/"})
  @PreAuthorize("hasAuthority('salespeople:delete')")
  public void delete(@PathVariable String idNumber) throws NotFoundException {
    crudService.delete(whereIdNumberIs(idNumber));
  }

  private Predicate whereIdNumberIs(String idNumber) {
    Map<String, String> idNumberMatcher = Maps.of("idnumber", idNumber).build();
    return predicateService.parseMap(idNumberMatcher);
  }
}
