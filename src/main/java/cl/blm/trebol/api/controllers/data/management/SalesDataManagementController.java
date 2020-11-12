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
import cl.blm.trebol.api.pojo.SellPojo;
import cl.blm.trebol.config.CustomProperties;
import cl.blm.trebol.jpa.entities.Sell;
import cl.blm.trebol.services.crud.GenericCrudService;

/**
 * API point of entry for Sell entities
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/data")
public class SalesDataManagementController
    extends GenericCrudController<SellPojo, Sell, Integer> {

  @Autowired
  public SalesDataManagementController(CustomProperties globals, GenericCrudService<SellPojo, Sell, Integer> crudService) {
    super(globals, crudService);
  }

  @Override
  @PostMapping("/sales")
  @PreAuthorize("hasAuthority('sales:create')")
  public Integer create(@RequestBody @Valid SellPojo input) {
    return super.create(input);
  }

  @Override
  @GetMapping("/sales/{id}")
  @PreAuthorize("hasAuthority('sales:read')")
  public SellPojo readOne(@PathVariable Integer id) {
    return super.readOne(id);
  }

  @GetMapping("/sales")
  @PreAuthorize("hasAuthority('sales:read')")
  public Collection<SellPojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    return super.readMany(null, null, allRequestParams);
  }

//  @GetMapping("/sales/{requestPageSize}")
//  @PreAuthorize("hasAuthority('sales:read')")
//  public Collection<SellPojo> readMany(@PathVariable Integer requestPageSize,
//      @RequestParam Map<String, String> allRequestParams) {
//    return super.readMany(requestPageSize, null, allRequestParams);
//  }
//
//  @Override
//  @GetMapping("/sales/{requestPageSize}/{requestPageIndex}")
//  @PreAuthorize("hasAuthority('sales:read')")
//  public Collection<SellPojo> readMany(@PathVariable Integer requestPageSize, @PathVariable Integer requestPageIndex,
//      @RequestParam Map<String, String> allRequestParams) {
//    return super.readMany(requestPageSize, requestPageIndex, allRequestParams);
//  }
//
//  @PutMapping("/sales")
//  @PreAuthorize("hasAuthority('sales:update')")
//  public Integer update(@RequestBody @Valid SellPojo input) {
//    return super.update(input, input.getId());
//  }

  @Override
  @PutMapping("/sales/{id}")
  @PreAuthorize("hasAuthority('sales:update')")
  public Integer update(@RequestBody @Valid SellPojo input, @PathVariable Integer id) {
    return super.update(input, id);
  }

  @Override
  @DeleteMapping("/sales/{id}")
  @PreAuthorize("hasAuthority('sales:delete')")
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
