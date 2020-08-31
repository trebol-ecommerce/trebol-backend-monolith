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
import cl.blm.newmarketing.backend.api.DataServiceClient;
import cl.blm.newmarketing.backend.api.GenericDataController;
import cl.blm.newmarketing.backend.api.pojo.SellPojo;
import cl.blm.newmarketing.backend.model.entities.Sell;
import cl.blm.newmarketing.backend.services.data.GenericDataService;

/**
 * API point of entry for Sell entities
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/api")
public class SalesDataController
    extends DataServiceClient<SellPojo, Sell, Integer>
    implements GenericDataController<SellPojo, Integer> {
  private final static Logger LOG = LoggerFactory.getLogger(SalesDataController.class);

  @Autowired
  public SalesDataController(CustomProperties globals, GenericDataService<SellPojo, Sell, Integer> crudService) {
    super(globals, crudService);
  }

  @PostMapping("/sell")
  public SellPojo create(@RequestBody @Valid SellPojo input) {
    LOG.info("create");
    SellPojo result = crudService.create(input);
    return result;
  }

  @GetMapping("/sell/{id}")
  public SellPojo readOne(@PathVariable Integer id) {
    LOG.info("read");
    SellPojo found = crudService.find(id);
    return found;
  }

  @GetMapping("/sales")
  public Collection<SellPojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    return this.readMany(null, null, allRequestParams);
  }

  @GetMapping("/sales/{requestPageSize}")
  public Collection<SellPojo> readMany(@PathVariable Integer requestPageSize,
      @RequestParam Map<String, String> allRequestParams) {
    return this.readMany(requestPageSize, null, allRequestParams);
  }

  @GetMapping("/sales/{requestPageSize}/{requestPageIndex}")
  public Collection<SellPojo> readMany(@PathVariable Integer requestPageSize, @PathVariable Integer requestPageIndex,
      @RequestParam Map<String, String> allRequestParams) {
    LOG.info("read");
    Collection<SellPojo> sales = this.readFromService(requestPageSize, requestPageIndex, allRequestParams);
    return sales;
  }

  @PutMapping("/sell")
  public SellPojo update(@RequestBody @Valid SellPojo input) {
    LOG.info("update");
    SellPojo result = crudService.update(input, input.id);
    return result;
  }

  @PutMapping("/sell/{id}")
  public SellPojo update(@RequestBody @Valid SellPojo input, @PathVariable Integer id) {
    LOG.info("update");
    SellPojo result = crudService.update(input, id);
    return result;
  }

  @DeleteMapping("/sell/{id}")
  public boolean delete(@PathVariable Integer id) {
    LOG.info("delete");
    boolean result = crudService.delete(id);
    return result;
  }
}
