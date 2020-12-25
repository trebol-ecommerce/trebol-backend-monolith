package cl.blm.trebol.api.controllers.data.management;

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
import cl.blm.trebol.api.pojo.CustomerPojo;
import cl.blm.trebol.config.CustomProperties;
import cl.blm.trebol.jpa.entities.Customer;
import cl.blm.trebol.services.crud.GenericCrudService;

/**
 * API point of entry for Customer entities
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/data")
public class ClientsDataManagementController
    extends GenericCrudController<CustomerPojo, Customer, Integer> {

  @Autowired
  public ClientsDataManagementController(CustomProperties globals,
      GenericCrudService<CustomerPojo, Customer, Integer> crudService) {
    super(globals, crudService);
  }

  @Override
  @PostMapping("/clients")
  @PreAuthorize("hasAuthority('clients:create')")
  public Integer create(@RequestBody @Valid CustomerPojo input) {
    return super.create(input);
  }

  @Override
  @GetMapping("/clients/{id}")
  @PreAuthorize("hasAuthority('clients:read')")
  public CustomerPojo readOne(@PathVariable Integer id) {
    return super.readOne(id);
  }

  @GetMapping("/clients")
  @PreAuthorize("hasAuthority('clients:read')")
  public Collection<CustomerPojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    return super.readMany(null, null, allRequestParams);
  }

  @GetMapping("/clients/{requestPageSize}")
  @PreAuthorize("hasAuthority('clients:read')")
  public Collection<CustomerPojo> readMany(@PathVariable Integer requestPageSize,
      @RequestParam Map<String, String> allRequestParams) {
    return super.readMany(requestPageSize, null, allRequestParams);
  }

  @Override
  @GetMapping("/clients/{requestPageSize}/{requestPageIndex}")
  @PreAuthorize("hasAuthority('clients:read')")
  public Collection<CustomerPojo> readMany(@PathVariable Integer requestPageSize, @PathVariable Integer requestPageIndex,
      @RequestParam Map<String, String> allRequestParams) {
    return super.readMany(requestPageSize, requestPageIndex, allRequestParams);
  }

  @PutMapping("/clients")
  @PreAuthorize("hasAuthority('clients:update')")
  public Integer update(@RequestBody @Valid CustomerPojo input) {
    return super.update(input, input.getId());
  }

  @Override
  @PutMapping("/clients/{id}")
  @PreAuthorize("hasAuthority('clients:update')")
  public Integer update(@RequestBody @Valid CustomerPojo input, @PathVariable Integer id) {
    return super.update(input, id);
  }

  @Override
  @DeleteMapping("/clients/{id}")
  @PreAuthorize("hasAuthority('clients:delete')")
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
