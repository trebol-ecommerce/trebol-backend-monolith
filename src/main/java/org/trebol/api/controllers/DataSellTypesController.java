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

import org.trebol.api.DataPage;
import org.trebol.api.GenericDataController;
import org.trebol.api.pojo.SellTypePojo;
import org.trebol.config.CustomProperties;
import org.trebol.jpa.entities.SellType;
import org.trebol.jpa.exceptions.EntityAlreadyExistsException;
import org.trebol.jpa.services.GenericJpaCrudService;

/**
 * API point of entry for SellType entities
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/data/sell_types")
public class DataSellTypesController
  extends GenericDataController<SellTypePojo, SellType>
  implements CrudController<SellTypePojo, String> {

  @Autowired
  public DataSellTypesController(CustomProperties globals,
      GenericJpaCrudService<SellTypePojo, SellType> crudService) {
    super(globals, crudService);
  }

  @GetMapping({"", "/"})
  @PreAuthorize("hasAuthority('sell_types:read')")
  public DataPage<SellTypePojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    return super.readMany(null, null, allRequestParams);
  }

  @Override
  @PostMapping({"", "/"})
  @PreAuthorize("hasAuthority('sell_types:create')")
  public void create(
    @RequestBody @Valid SellTypePojo input
  ) throws EntityAlreadyExistsException {
    crudService.create(input);
  }

  @Override
  @GetMapping({"/{code}", "/{code}/"})
  @PreAuthorize("hasAuthority('sell_types:read')")
  public SellTypePojo readOne(@PathVariable String code) {
    throw new UnsupportedOperationException("Method not implemented");
  }

  @Override
  @PutMapping({"/{code}", "/{code}/"})
  @PreAuthorize("hasAuthority('sell_types:update')")
  public void update(@RequestBody @Valid SellTypePojo input, @PathVariable String code) {
    throw new UnsupportedOperationException("Method not implemented");
  }

  @Override
  @DeleteMapping({"/{code}", "/{code}/"})
  @PreAuthorize("hasAuthority('sell_types:delete')")
  public void delete(@PathVariable String code) {
    throw new UnsupportedOperationException("Method not implemented");
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