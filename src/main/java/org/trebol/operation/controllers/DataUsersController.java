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
 * API point of entry for User entities
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/data/users")
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
  @GetMapping({"/{name}", "/{name}/"})
  @PreAuthorize("hasAuthority('users:read')")
  public UserPojo readOne(@PathVariable String name) throws NotFoundException {
    Map<String, String> nameMatcher = Maps.of("name", name).build();
    Predicate matchesName = crudService.parsePredicate(nameMatcher);
    return crudService.readOne(matchesName);
  }

  @Override
  @PutMapping({"/{name}", "/{name}/"})
  @PreAuthorize("hasAuthority('users:update')")
  public void update(@RequestBody UserPojo input, @PathVariable String name)
    throws BadInputException, NotFoundException {
    Long userId = this.readOne(name).getId();
    crudService.update(input, userId);
  }

  @Override
  public void delete(@PathVariable String name) throws NotFoundException {
    Long userId = this.readOne(name).getId();
    crudService.delete(userId);
  }


  @DeleteMapping({"/{name}", "/{name}/"})
  @PreAuthorize("hasAuthority('users:delete')")
  public void delete(Principal principal, @PathVariable String name) throws NotFoundException, BadInputException {
    if(principal.getName().equals(name)){
      throw new BadInputException("A user should not be able to delete their own account");
    }
    this.delete(name);
  }
}
