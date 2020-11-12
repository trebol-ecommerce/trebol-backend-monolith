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
import cl.blm.trebol.api.pojo.SellTypePojo;
import cl.blm.trebol.config.CustomProperties;
import cl.blm.trebol.jpa.entities.SellType;
import cl.blm.trebol.services.crud.GenericCrudService;

/**
 * API point of entry for Sell entities
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/api")
public class SellTypesCrudController
    extends GenericCrudController<SellTypePojo, SellType, Integer> {

  @Autowired
  public SellTypesCrudController(CustomProperties globals,
      GenericCrudService<SellTypePojo, SellType, Integer> crudService) {
    super(globals, crudService);
  }

  @Override
  @PostMapping("/sell_type")
  public Integer create(@RequestBody @Valid SellTypePojo input) {
    return super.create(input);
  }

  @Override
  @GetMapping("/sell_type/{id}")
  @PreAuthorize("hasAuthority('sell_types:read')")
  public SellTypePojo readOne(@PathVariable Integer id) {
    return super.readOne(id);
  }

  @GetMapping("/sell_types")
  @PreAuthorize("hasAuthority('sell_types:read')")
  public Collection<SellTypePojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    return super.readMany(null, null, allRequestParams);
  }

  @GetMapping("/sell_types/{requestPageSize}")
  @PreAuthorize("hasAuthority('sell_types:read')")
  public Collection<SellTypePojo> readMany(@PathVariable Integer requestPageSize,
      @RequestParam Map<String, String> allRequestParams) {
    return super.readMany(requestPageSize, null, allRequestParams);
  }

  @Override
  @GetMapping("/sell_types/{requestPageSize}/{requestPageIndex}")
  @PreAuthorize("hasAuthority('sell_types:read')")
  public Collection<SellTypePojo> readMany(@PathVariable Integer requestPageSize,
      @PathVariable Integer requestPageIndex, @RequestParam Map<String, String> allRequestParams) {
    return super.readMany(requestPageSize, requestPageIndex, allRequestParams);
  }

  @PutMapping("/sell_type")
  @PreAuthorize("hasAuthority('sell_types:update')")
  public Integer update(@RequestBody @Valid SellTypePojo input) {
    return super.update(input, input.getId());
  }

  @Override
  @PutMapping("/sell_type/{id}")
  @PreAuthorize("hasAuthority('sell_types:update')")
  public Integer update(@RequestBody @Valid SellTypePojo input, @PathVariable Integer id) {
    return super.update(input, id);
  }

  @Override
  @DeleteMapping("/sell_type/{id}")
  @PreAuthorize("hasAuthority('sell_types:delete')")
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
