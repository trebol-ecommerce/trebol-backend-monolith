package cl.blm.newmarketing.backend.api.controllers;

import java.util.Collection;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import cl.blm.newmarketing.backend.CustomProperties;
import cl.blm.newmarketing.backend.api.GenericEntityDataController;
import cl.blm.newmarketing.backend.api.pojo.SellPojo;
import cl.blm.newmarketing.backend.jpa.entities.Sell;
import cl.blm.newmarketing.backend.services.data.GenericEntityDataService;

/**
 * API point of entry for Sell entities
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/api")
public class SalesDataController
    extends GenericEntityDataController<SellPojo, Sell, Integer> {
  private final static Logger LOG = LoggerFactory.getLogger(SalesDataController.class);

  @Autowired
  public SalesDataController(CustomProperties globals, GenericEntityDataService<SellPojo, Sell, Integer> crudService) {
    super(LOG, globals, crudService);
  }

  @Override
  @PostMapping("/sell")
  public Integer create(@RequestBody @Valid SellPojo input) {
    return super.create(input);
  }

  @Override
  @GetMapping("/sell/{id}")
  public SellPojo readOne(@PathVariable Integer id) {
    return super.readOne(id);
  }

  @GetMapping("/sales")
  public Collection<SellPojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    return super.readMany(null, null, allRequestParams);
  }

  @GetMapping("/sales/{requestPageSize}")
  public Collection<SellPojo> readMany(@PathVariable Integer requestPageSize,
      @RequestParam Map<String, String> allRequestParams) {
    return super.readMany(requestPageSize, null, allRequestParams);
  }

  @Override
  @GetMapping("/sales/{requestPageSize}/{requestPageIndex}")
  public Collection<SellPojo> readMany(@PathVariable Integer requestPageSize, @PathVariable Integer requestPageIndex,
      @RequestParam Map<String, String> allRequestParams) {
    return super.readMany(requestPageSize, requestPageIndex, allRequestParams);
  }

  @PutMapping("/sell")
  public Integer update(@RequestBody @Valid SellPojo input) {
    return super.update(input, input.getId());
  }

  @Override
  @PutMapping("/sell/{id}")
  public Integer update(@RequestBody @Valid SellPojo input, @PathVariable Integer id) {
    return super.update(input, id);
  }

  @Override
  @DeleteMapping("/sell/{id}")
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