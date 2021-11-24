package org.trebol.operation.controllers;

import java.security.Principal;
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

import org.trebol.pojo.DataPagePojo;
import org.trebol.operation.GenericDataController;
import org.trebol.pojo.ProductPojo;
import org.trebol.pojo.UserPojo;
import org.trebol.config.OperationProperties;
import org.trebol.jpa.entities.User;
import org.trebol.exceptions.EntityAlreadyExistsException;
import org.trebol.jpa.GenericJpaService;
import org.trebol.operation.IDataCrudController;
import org.trebol.exceptions.BadInputException;

import com.querydsl.core.types.Predicate;

import javassist.NotFoundException;

/**
 * Controller that maps API resources for CRUD operations on Users
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/data/users")
@PreAuthorize("isAuthenticated()")
public class DataUsersController
  extends GenericDataController<UserPojo, User>
  implements IDataCrudController<UserPojo, String> {

  @Autowired
  public DataUsersController(OperationProperties globals, GenericJpaService<UserPojo, User> crudService) {
    super(globals, crudService);
  }

  @GetMapping({"", "/"})
  @PreAuthorize("hasAuthority('users:read')")
  public DataPagePojo<UserPojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    return super.readMany(null, null, allRequestParams);
  }

  @Override
  @PostMapping({"", "/"})
  @PreAuthorize("hasAuthority('users:create')")
  public void create(@Valid @RequestBody UserPojo input) throws BadInputException, EntityAlreadyExistsException {
    crudService.create(input);
  }

  @Override
  @PutMapping({"", "/"})
  @PreAuthorize("hasAuthority('users:update')")
  public void update(@RequestBody UserPojo input, @RequestParam Map<String, String> requestParams)
      throws BadInputException, NotFoundException {
    if (!requestParams.isEmpty()) {
      Predicate predicate = crudService.parsePredicate(requestParams);
      UserPojo existing = crudService.readOne(predicate);
      crudService.update(input, existing.getId());
    } else {
      crudService.update(input);
    }
  }

  @DeleteMapping({"", "/"})
  @PreAuthorize("hasAuthority('users:delete')")
  public void delete(Principal principal, @RequestParam Map<String, String> requestParams) throws NotFoundException, BadInputException {
    if (requestParams.containsKey("name") && requestParams.get("name").equals(principal.getName())) {
      throw new BadInputException("A user should not be able to delete their own account");
    }
    this.delete(requestParams);
  }


  @Override
  public void delete(@RequestParam Map<String, String> requestParams) throws NotFoundException {
    Predicate predicate = crudService.parsePredicate(requestParams);
    crudService.delete(predicate);
  }

  @Deprecated
  @GetMapping({"/{name}", "/{name}/"})
  @PreAuthorize("hasAuthority('users:read')")
  public UserPojo readOne(@PathVariable String name) throws NotFoundException {
    Map<String, String> nameMatcher = Maps.of("name", name).build();
    Predicate matchesName = crudService.parsePredicate(nameMatcher);
    return crudService.readOne(matchesName);
  }

  @Deprecated
  @PutMapping({"/{name}", "/{name}/"})
  @PreAuthorize("hasAuthority('users:update')")
  public void update(@RequestBody UserPojo input, @PathVariable String name)
    throws BadInputException, NotFoundException {
    Long userId = this.readOne(name).getId();
    crudService.update(input, userId);
  }

  @Deprecated
  @DeleteMapping({"/{name}", "/{name}/"})
  @PreAuthorize("hasAuthority('users:delete')")
  public void delete(Principal principal, @PathVariable String name) throws NotFoundException, BadInputException {
    if (principal.getName().equals(name)){
      throw new BadInputException("A user should not be able to delete their own account");
    }
    Long userId = this.readOne(name).getId();
    crudService.delete(userId);
  }
}
