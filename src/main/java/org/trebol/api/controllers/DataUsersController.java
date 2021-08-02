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

import org.trebol.api.GenericCrudController;
import org.trebol.api.DataPage;
import org.trebol.api.pojo.UserPojo;
import org.trebol.config.CustomProperties;
import org.trebol.jpa.entities.User;
import org.trebol.jpa.services.GenericCrudService;

/**
 * API point of entry for User entities
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/data/users")
public class DataUsersController
    extends GenericCrudController<UserPojo, User, Integer> {

  @Autowired
  public DataUsersController(CustomProperties globals,
      GenericCrudService<UserPojo, User, Integer> crudService) {
    super(globals, crudService);
  }

  @GetMapping({"", "/"})
  @PreAuthorize("hasAuthority('users:read')")
  public DataPage<UserPojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    return super.readMany(null, null, allRequestParams);
  }

  @Override
  @PostMapping({"", "/"})
  @PreAuthorize("hasAuthority('users:create')")
  public void create(@RequestBody @Valid UserPojo input) {
    super.create(input);
  }

  @Override
  @GetMapping({"/{id}", "/{id}/"})
  @PreAuthorize("hasAuthority('users:read')")
  public UserPojo readOne(@PathVariable Integer id) {
    return super.readOne(id);
  }

  @Override
  @PutMapping({"/{id}", "/{id}/"})
  @PreAuthorize("hasAuthority('users:update')")
  public void update(@RequestBody @Valid UserPojo input, @PathVariable Integer id) {
    super.update(input, id);
  }

  @Override
  @DeleteMapping({"/{id}", "/{id}/"})
  @PreAuthorize("hasAuthority('users:delete')")
  public void delete(@PathVariable Integer id) {
    super.delete(id);
  }

  @Override
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
    return super.handleValidationExceptions(ex);
  }
}
