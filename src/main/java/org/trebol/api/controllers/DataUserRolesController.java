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
import org.trebol.api.pojo.UserRolePojo;
import org.trebol.config.CustomProperties;
import org.trebol.jpa.entities.UserRole;
import org.trebol.exceptions.EntityAlreadyExistsException;
import org.trebol.jpa.GenericJpaCrudService;
import org.trebol.api.IDataCrudController;
import org.trebol.exceptions.BadInputException;

import com.querydsl.core.types.Predicate;

import javassist.NotFoundException;

/**
 * API point of entry for UserRole entities
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/data/user_roles")
public class DataUserRolesController
  extends GenericDataController<UserRolePojo, UserRole>
  implements IDataCrudController<UserRolePojo, String> {

  @Autowired
  public DataUserRolesController(CustomProperties globals, GenericJpaCrudService<UserRolePojo, UserRole> crudService) {
    super(globals, crudService);
  }

  @GetMapping({"", "/"})
  @PreAuthorize("hasAuthority('user_roles:read')")
  public DataPage<UserRolePojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    return super.readMany(null, null, allRequestParams);
  }

  @Override
  @PostMapping({"", "/"})
  @PreAuthorize("hasAuthority('user_roles:create')")
  public void create(@Valid @RequestBody UserRolePojo input) throws BadInputException, EntityAlreadyExistsException {
    crudService.create(input);
  }

  @Override
  @GetMapping({"/{code}", "/{code}/"})
  @PreAuthorize("hasAuthority('user_roles:read')")
  public UserRolePojo readOne(@PathVariable String code) throws NotFoundException {
    Map<String, String> codeMatcher = Maps.of("code", code).build();
    Predicate matchesCode = crudService.parsePredicate(codeMatcher);
    return crudService.readOne(matchesCode);
  }

  @Override
  @PutMapping({"/{code}", "/{code}/"})
  @PreAuthorize("hasAuthority('user_roles:update')")
  public void update(@RequestBody UserRolePojo input, @PathVariable String code)
    throws BadInputException, NotFoundException {
    Long userRoleId = this.readOne(code).getId();
    crudService.update(input, userRoleId);
  }

  @Override
  @DeleteMapping({"/{code}", "/{code}/"})
  @PreAuthorize("hasAuthority('user_roles:delete')")
  public void delete(@PathVariable String code) throws NotFoundException {
    Long userRoleId = this.readOne(code).getId();
    crudService.delete(userRoleId);
  }
}
