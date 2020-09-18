package cl.blm.newmarketing.store.api.controllers.crud;

import java.util.Collection;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import cl.blm.newmarketing.store.api.GenericEntityDataController;
import cl.blm.newmarketing.store.api.pojo.SellerPojo;
import cl.blm.newmarketing.store.config.CustomProperties;
import cl.blm.newmarketing.store.jpa.entities.Seller;
import cl.blm.newmarketing.store.services.crud.GenericEntityCrudService;

/**
 * API point of entry for Seller entities
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/api")
public class SellersCrudController
    extends GenericEntityDataController<SellerPojo, Seller, Integer> {

  @Autowired
  public SellersCrudController(CustomProperties globals,
      GenericEntityCrudService<SellerPojo, Seller, Integer> crudService) {
    super(globals, crudService);
  }

  @Override
  @PostMapping("/seller")
  public Integer create(@RequestBody @Valid SellerPojo input) {
    return super.create(input);
  }

  @Override
  @GetMapping("/seller/{id}")
  public SellerPojo readOne(@PathVariable Integer id) {
    return super.readOne(id);
  }

  @GetMapping("/sellers")
  public Collection<SellerPojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    return super.readMany(null, null, allRequestParams);
  }

  @GetMapping("/sellers/{requestPageSize}")
  public Collection<SellerPojo> readMany(@PathVariable Integer requestPageSize,
      @RequestParam Map<String, String> allRequestParams) {
    return super.readMany(requestPageSize, null, allRequestParams);
  }

  @Override
  @GetMapping("/sellers/{requestPageSize}/{requestPageIndex}")
  public Collection<SellerPojo> readMany(@PathVariable Integer requestPageSize, @PathVariable Integer requestPageIndex,
      @RequestParam Map<String, String> allRequestParams) {
    return super.readMany(requestPageSize, requestPageIndex, allRequestParams);
  }

  @PutMapping("/seller")
  public Integer update(@RequestBody @Valid SellerPojo input) {
    return super.update(input, input.getId());
  }

  @Override
  @PutMapping("/seller/{id}")
  public Integer update(@RequestBody @Valid SellerPojo input, @PathVariable Integer id) {
    return super.update(input, id);
  }

  @Override
  @DeleteMapping("/seller/{id}")
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
