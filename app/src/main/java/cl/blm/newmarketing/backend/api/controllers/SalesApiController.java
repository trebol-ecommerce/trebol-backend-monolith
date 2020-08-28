package cl.blm.newmarketing.backend.api.controllers;

import java.util.Collection;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.blm.newmarketing.backend.CustomProperties;
import cl.blm.newmarketing.backend.api.ApiCrudController;
import cl.blm.newmarketing.backend.api.DtoCrudServiceClient;
import cl.blm.newmarketing.backend.model.entities.Sell;
import cl.blm.newmarketing.backend.services.data.GenericDataService;

/**
 * API point of entry for Sell entities
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/api")
public class SalesApiController
    extends DtoCrudServiceClient<Sell, Integer>
    implements ApiCrudController<Sell, Integer> {
  private final static Logger LOG = LoggerFactory.getLogger(SalesApiController.class);

  @Autowired
  public SalesApiController(CustomProperties globals, GenericDataService<Sell, Integer> crudService) {
    super(globals, crudService);
  }

  @PostMapping("/sell")
  public Sell create(@RequestBody @Valid Sell input) {
    LOG.info("create");
    Sell result = crudService.create(input);
    return result;
  }

  @GetMapping("/sell/{id}")
  public Sell readOne(@PathVariable Integer id) {
    LOG.info("read");
    Sell found = crudService.find(id);
    return found;
  }

  @GetMapping("/sales")
  public Collection<Sell> readMany(@RequestParam Map<String, String> allRequestParams) {
    return this.readMany(null, null, allRequestParams);
  }

  @GetMapping("/sales/{requestPageSize}")
  public Collection<Sell> readMany(@PathVariable Integer requestPageSize,
      @RequestParam Map<String, String> allRequestParams) {
    return this.readMany(requestPageSize, null, allRequestParams);
  }

  @GetMapping("/sales/{requestPageSize}/{requestPageIndex}")
  public Collection<Sell> readMany(@PathVariable Integer requestPageSize, @PathVariable Integer requestPageIndex,
      @RequestParam Map<String, String> allRequestParams) {
    LOG.info("read");
    Collection<Sell> sales = this.readFromService(requestPageSize, requestPageIndex, allRequestParams);
    return sales;
  }

  @PutMapping("/sell")
  public Sell update(@RequestBody @Valid Sell input) {
    LOG.info("update");
    Sell result = crudService.update(input, input.getId());
    return result;
  }

  @PutMapping("/sell/{id}")
  public Sell update(@RequestBody @Valid Sell input, @PathVariable Integer id) {
    LOG.info("update");
    Sell result = crudService.update(input, id);
    return result;
  }

  @DeleteMapping("/sell/{id}")
  public boolean delete(@PathVariable Integer id) {
    LOG.info("delete");
    boolean result = crudService.delete(id);
    return result;
  }
}
