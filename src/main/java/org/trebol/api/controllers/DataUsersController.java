package org.trebol.api.controllers;

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

import org.trebol.api.DataPage;
import org.trebol.api.GenericDataController;
import org.trebol.api.pojo.UserPojo;
import org.trebol.config.CustomProperties;
import org.trebol.jpa.entities.User;
import org.trebol.exceptions.EntityAlreadyExistsException;
import org.trebol.jpa.GenericJpaCrudService;
import org.trebol.api.IDataCrudController;
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
  public DataUsersController(CustomProperties globals, GenericJpaCrudService<UserPojo, User> crudService) {
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
  public void create(@RequestBody @Valid UserPojo input) throws BadInputException, EntityAlreadyExistsException {
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
  public void update(@RequestBody @Valid UserPojo input, @PathVariable String name)
    throws BadInputException, NotFoundException {
    Long userId = this.readOne(name).getId();
    crudService.update(input, userId);
  }

  @Override
  @DeleteMapping({"/{name}", "/{name}/"})
  @PreAuthorize("hasAuthority('users:delete')")
  public void delete(@PathVariable String name) throws NotFoundException {
    Long userId = this.readOne(name).getId();
    crudService.delete(userId);
  }
}
