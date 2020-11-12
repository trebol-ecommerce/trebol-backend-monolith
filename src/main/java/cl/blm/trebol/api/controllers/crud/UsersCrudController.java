package cl.blm.trebol.api.controllers.crud;

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

import cl.blm.trebol.api.GenericCrudController;
import cl.blm.trebol.api.pojo.UserPojo;
import cl.blm.trebol.config.CustomProperties;
import cl.blm.trebol.jpa.entities.User;
import cl.blm.trebol.services.crud.GenericCrudService;

/**
 * API point of entry for User entities
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/api")
public class UsersCrudController
    extends GenericCrudController<UserPojo, User, Integer> {

  @Autowired
  public UsersCrudController(CustomProperties globals,
      GenericCrudService<UserPojo, User, Integer> crudService) {
    super(globals, crudService);
  }

  @Override
  @PostMapping("/user")
  @PreAuthorize("hasAuthority('users:create')")
  public Integer create(@RequestBody @Valid UserPojo input) {
    return super.create(input);
  }

  @Override
  @GetMapping("/user/{id}")
  @PreAuthorize("hasAuthority('users:read')")
  public UserPojo readOne(@PathVariable Integer id) {
    return super.readOne(id);
  }

  @GetMapping("/users")
  @PreAuthorize("hasAuthority('users:read')")
  public Collection<UserPojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    return super.readMany(null, null, allRequestParams);
  }

  @GetMapping("/users/{requestPageSize}")
  @PreAuthorize("hasAuthority('users:read')")
  public Collection<UserPojo> readMany(@PathVariable Integer requestPageSize,
      @RequestParam Map<String, String> allRequestParams) {
    return super.readMany(requestPageSize, null, allRequestParams);
  }

  @Override
  @GetMapping("/users/{requestPageSize}/{requestPageIndex}")
  @PreAuthorize("hasAuthority('users:read')")
  public Collection<UserPojo> readMany(@PathVariable Integer requestPageSize, @PathVariable Integer requestPageIndex,
      @RequestParam Map<String, String> allRequestParams) {
    return super.readMany(requestPageSize, requestPageIndex, allRequestParams);
  }

  @PutMapping("/user")
  @PreAuthorize("hasAuthority('users:update')")
  public Integer update(@RequestBody @Valid UserPojo input) {
    return super.update(input, input.getId());
  }

  @Override
  @PutMapping("/user/{id}")
  @PreAuthorize("hasAuthority('users:update')")
  public Integer update(@RequestBody @Valid UserPojo input, @PathVariable Integer id) {
    return super.update(input, id);
  }

  @Override
  @DeleteMapping("/user/{id}")
  @PreAuthorize("hasAuthority('users:delete')")
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
