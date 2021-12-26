package org.trebol.operation.controllers;

import com.querydsl.core.types.Predicate;
import io.jsonwebtoken.lang.Maps;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.trebol.config.OperationProperties;
import org.trebol.exceptions.BadInputException;
import org.trebol.exceptions.EntityAlreadyExistsException;
import org.trebol.jpa.entities.User;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.IPredicateJpaService;
import org.trebol.jpa.services.ISortJpaService;
import org.trebol.operation.GenericDataCrudController;
import org.trebol.pojo.DataPagePojo;
import org.trebol.pojo.UserPojo;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;

/**
 * Controller that maps API resources for CRUD operations on Users
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/data/users")
@PreAuthorize("isAuthenticated()")
public class DataUsersController
  extends GenericDataCrudController<UserPojo, User> {

  private final ISortJpaService<User> sortService;

  @Autowired
  public DataUsersController(OperationProperties globals,
                             GenericCrudJpaService<UserPojo, User> crudService,
                             IPredicateJpaService<User> predicateService,
                             ISortJpaService<User> sortService) {
    super(globals, crudService, predicateService);
    this.sortService = sortService;
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
    super.create(input);
  }

  @Override
  @PutMapping({"", "/"})
  @PreAuthorize("hasAuthority('users:update')")
  public void update(@RequestBody UserPojo input, @RequestParam Map<String, String> requestParams)
      throws BadInputException, NotFoundException {
    super.update(input, requestParams);
  }

  @DeleteMapping({"", "/"})
  @PreAuthorize("hasAuthority('users:delete')")
  public void delete(Principal principal, @RequestParam Map<String, String> requestParams) throws NotFoundException, BadInputException {
    if (requestParams.containsKey("name") && requestParams.get("name").equals(principal.getName())) {
      throw new BadInputException("A user should not be able to delete their own account");
    }
    super.delete(requestParams);
  }

  @Deprecated(forRemoval = true)
  @GetMapping({"/{name}", "/{name}/"})
  @PreAuthorize("hasAuthority('users:read')")
  public UserPojo readOne(@PathVariable String name) throws NotFoundException {
    return crudService.readOne(whereNameIs(name));
  }

  @Deprecated(forRemoval = true)
  @PutMapping({"/{name}", "/{name}/"})
  @PreAuthorize("hasAuthority('users:update')")
  public void update(@RequestBody UserPojo input, @PathVariable String name)
    throws BadInputException, NotFoundException {
    crudService.update(input, whereNameIs(name));
  }

  @Deprecated(forRemoval = true)
  @DeleteMapping({"/{name}", "/{name}/"})
  @PreAuthorize("hasAuthority('users:delete')")
  public void delete(Principal principal, @PathVariable String name) throws NotFoundException, BadInputException {
    if (principal.getName().equals(name)){
      throw new BadInputException("A user should not be able to delete their own account");
    }
    crudService.delete(whereNameIs(name));
  }

  private Predicate whereNameIs(String name) {
    Map<String, String> nameMatcher = Maps.of("name", name).build();
    return predicateService.parseMap(nameMatcher);
  }

  @Override
  protected Sort determineSortOrder(Map<String, String> requestParams) {
    return sortService.parseMap(requestParams);
  }
}
