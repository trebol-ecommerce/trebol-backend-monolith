package org.trebol.api.controllers;

import java.util.Collection;
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

import org.trebol.api.GenericCrudController;
import org.trebol.api.pojo.UserRolePojo;
import org.trebol.config.CustomProperties;
import org.trebol.jpa.entities.UserRole;
import org.trebol.jpa.services.GenericCrudService;

/**
 * API point of entry for UserRole entities
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/data/user_roles")
public class DataUserRolesController
    extends GenericCrudController<UserRolePojo, UserRole, Integer> {

  @Autowired
  public DataUserRolesController(CustomProperties globals,
      GenericCrudService<UserRolePojo, UserRole, Integer> crudService) {
    super(globals, crudService);
  }

  @GetMapping({"", "/"})
  @PreAuthorize("hasAuthority('user_roles:read')")
  public Collection<UserRolePojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    return super.readMany(null, null, allRequestParams);
  }

  @Override
  @PostMapping({"", "/"})
  @PreAuthorize("hasAuthority('user_roles:create')")
  public Integer create(@RequestBody @Valid UserRolePojo input) {
    return super.create(input);
  }

  @Override
  @GetMapping({"/{id}", "/{id}/"})
  @PreAuthorize("hasAuthority('user_roles:read')")
  public UserRolePojo readOne(@PathVariable Integer id) {
    return super.readOne(id);
  }

  @Override
  @PutMapping({"/{id}", "/{id}/"})
  @PreAuthorize("hasAuthority('user_roles:update')")
  public Integer update(@RequestBody @Valid UserRolePojo input, @PathVariable Integer id) {
    return super.update(input, id);
  }

  @Override
  @DeleteMapping({"/{id}", "/{id}/"})
  @PreAuthorize("hasAuthority('user_roles:delete')")
  public boolean delete(@PathVariable Integer id) {
    return super.delete(id);
  }

  @Override
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
    return super.handleValidationExceptions(ex);
  }
}
