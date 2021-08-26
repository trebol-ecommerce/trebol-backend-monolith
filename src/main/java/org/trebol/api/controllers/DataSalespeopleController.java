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
import org.trebol.api.CrudController;

import org.trebol.api.DataPage;
import org.trebol.api.GenericDataController;
import org.trebol.api.pojo.SalespersonPojo;
import org.trebol.config.CustomProperties;
import org.trebol.jpa.entities.Salesperson;
import org.trebol.jpa.exceptions.EntityAlreadyExistsException;
import org.trebol.jpa.services.GenericJpaCrudService;

import com.querydsl.core.types.Predicate;

/**
 * API point of entry for Salesperson entities
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/data/salespeople")
public class DataSalespeopleController
  extends GenericDataController<SalespersonPojo, Salesperson>
  implements CrudController<SalespersonPojo, String> {

  @Autowired
  public DataSalespeopleController(CustomProperties globals,
    GenericJpaCrudService<SalespersonPojo, Salesperson> crudService) {
    super(globals, crudService);
  }

  @GetMapping({"", "/"})
  @PreAuthorize("hasAuthority('salespeople:read')")
  public DataPage<SalespersonPojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    return super.readMany(null, null, allRequestParams);
  }

  @Override
  @PostMapping({"", "/"})
  @PreAuthorize("hasAuthority('salespeople:create')")
  public void create(@RequestBody @Valid SalespersonPojo input) throws EntityAlreadyExistsException {
    crudService.create(input);
  }

  @Override
  @GetMapping({"/{idCard}", "/{idCard}/"})
  @PreAuthorize("hasAuthority('salespeople:read')")
  public SalespersonPojo readOne(@PathVariable String idCard) {
    Map<String, String> idCardMatchMap = Maps.of("idnumber", idCard).build();
    Predicate filters = crudService.queryParamsMapToPredicate(idCardMatchMap);
    return crudService.readOne(filters);
  }

  @Override
  @PutMapping({"/{idCard}", "/{idCard}/"})
  @PreAuthorize("hasAuthority('salespeople:update')")
  public void update(@RequestBody @Valid SalespersonPojo input, @PathVariable String idCard) {
     // TODO improve this implementation; the same salesperson will be fetched twice
    Long salespersonId = this.readOne(idCard).getId();
    crudService.update(input, salespersonId);
  }

  @Override
  @DeleteMapping({"/{idCard}", "/{idCard}/"})
  @PreAuthorize("hasAuthority('salespeople:delete')")
  public void delete(@PathVariable String idCard) {
    Long salespersonId = this.readOne(idCard).getId();
    crudService.delete(salespersonId);
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
