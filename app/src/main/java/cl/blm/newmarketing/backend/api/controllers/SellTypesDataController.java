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
import cl.blm.newmarketing.backend.api.pojo.SellTypePojo;
import cl.blm.newmarketing.backend.model.entities.SellType;
import cl.blm.newmarketing.backend.services.data.GenericDataService;

/**
 * API point of entry for Sell entities
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/api")
public class SellTypesDataController
    extends DataServiceClient<SellTypePojo, SellType, Integer>
    implements GenericDataController<SellTypePojo, Integer> {
  private final static Logger LOG = LoggerFactory.getLogger(SellTypesDataController.class);

  @Autowired
  public SellTypesDataController(CustomProperties globals,
      GenericDataService<SellTypePojo, SellType, Integer> crudService) {
    super(globals, crudService);
  }

  @PostMapping("/sell_type")
  public Integer create(@RequestBody @Valid SellTypePojo input) {
    LOG.info("create");
    Integer resultId = crudService.create(input);
    return resultId;
  }

  @GetMapping("/sell_type/{id}")
  public SellTypePojo readOne(@PathVariable Integer id) {
    LOG.info("read");
    SellTypePojo found = crudService.find(id);
    return found;
  }

  @GetMapping("/sell_types")
  public Collection<SellTypePojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    return this.readMany(null, null, allRequestParams);
  }

  @GetMapping("/sell_types/{requestPageSize}")
  public Collection<SellTypePojo> readMany(@PathVariable Integer requestPageSize,
      @RequestParam Map<String, String> allRequestParams) {
    return this.readMany(requestPageSize, null, allRequestParams);
  }

  @GetMapping("/sell_types/{requestPageSize}/{requestPageIndex}")
  public Collection<SellTypePojo> readMany(@PathVariable Integer requestPageSize,
      @PathVariable Integer requestPageIndex, @RequestParam Map<String, String> allRequestParams) {
    LOG.info("read");
    Collection<SellTypePojo> sales = this.readFromService(requestPageSize, requestPageIndex, allRequestParams);
    return sales;
  }

  @PutMapping("/sell_type")
  public Integer update(@RequestBody @Valid SellTypePojo input) {
    LOG.info("update");
    Integer resultId = crudService.update(input, input.getId());
    return resultId;
  }

  @PutMapping("/sell_type/{id}")
  public Integer update(@RequestBody @Valid SellTypePojo input, @PathVariable Integer id) {
    LOG.info("update");
    Integer resultId = crudService.update(input, id);
    return resultId;
  }

  @DeleteMapping("/sell_type/{id}")
  public boolean delete(@PathVariable Integer id) {
    LOG.info("delete");
    boolean result = crudService.delete(id);
    return result;
  }
}
