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
import org.trebol.jpa.entities.UserRole;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.IPredicateJpaService;
import org.trebol.operation.GenericDataCrudController;
import org.trebol.pojo.DataPagePojo;
import org.trebol.pojo.UserRolePojo;

import javax.validation.Valid;
import java.util.Map;

/**
 * Controller that maps API resources for CRUD operations on UserRoles
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/data/user_roles")
@PreAuthorize("isAuthenticated()")
public class DataUserRolesController
  extends GenericDataCrudController<UserRolePojo, UserRole> {

  @Autowired
  public DataUserRolesController(OperationProperties globals,
                                 GenericCrudJpaService<UserRolePojo, UserRole> crudService,
                                 IPredicateJpaService<UserRole> predicateService) {
    super(globals, crudService, predicateService);
  }

  @GetMapping({"", "/"})
  @PreAuthorize("hasAuthority('user_roles:read')")
  public DataPagePojo<UserRolePojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    return super.readMany(null, null, allRequestParams);
  }

  @Override
  @PostMapping({"", "/"})
  @PreAuthorize("hasAuthority('user_roles:create')")
  public void create(@Valid @RequestBody UserRolePojo input) throws BadInputException, EntityAlreadyExistsException {
    super.create(input);
  }

  @Override
  @PutMapping({"", "/"})
  @PreAuthorize("hasAuthority('user_roles:update')")
  public void update(@RequestBody UserRolePojo input, @RequestParam Map<String, String> requestParams)
      throws BadInputException, NotFoundException {
    super.update(input, requestParams);
  }

  @Override
  @DeleteMapping({"", "/"})
  @PreAuthorize("hasAuthority('user_roles:delete')")
  public void delete(@RequestParam Map<String, String> requestParams) throws NotFoundException {
    super.delete(requestParams);
  }

  @Deprecated(forRemoval = true)
  @GetMapping({"/{code}", "/{code}/"})
  @PreAuthorize("hasAuthority('user_roles:read')")
  public UserRolePojo readOne(@PathVariable String code) throws NotFoundException {
    return crudService.readOne(whereCodeIs(code));
  }

  @Deprecated(forRemoval = true)
  @PutMapping({"/{code}", "/{code}/"})
  @PreAuthorize("hasAuthority('user_roles:update')")
  public void update(@RequestBody UserRolePojo input, @PathVariable String code)
    throws BadInputException, NotFoundException {
    crudService.update(input, whereCodeIs(code));
  }

  @Deprecated(forRemoval = true)
  @DeleteMapping({"/{code}", "/{code}/"})
  @PreAuthorize("hasAuthority('user_roles:delete')")
  public void delete(@PathVariable String code) throws NotFoundException {
    crudService.delete(whereCodeIs(code));
  }

  private Predicate whereCodeIs(String code) {
    Map<String, String> codeMatcher = Maps.of("code", code).build();
    return predicateService.parseMap(codeMatcher);
  }
}
