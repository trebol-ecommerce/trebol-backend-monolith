package org.trebol.api.controllers;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.trebol.api.GenericDataController;
import org.trebol.api.DataPage;
import org.trebol.api.pojo.SellPojo;
import org.trebol.config.CustomProperties;
import org.trebol.jpa.entities.Sell;
import org.trebol.exceptions.EntityAlreadyExistsException;
import org.trebol.jpa.GenericJpaCrudService;

import javassist.NotFoundException;

import org.trebol.api.IDataCrudController;
import org.trebol.exceptions.BadInputException;

/**
 * API point of entry for Sell entities
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/data/sales")
public class DataSalesController
  extends GenericDataController<SellPojo, Sell>
  implements IDataCrudController<SellPojo, Long> {

  @Autowired
  public DataSalesController(CustomProperties globals, GenericJpaCrudService<SellPojo, Sell> crudService) {
    super(globals, crudService);
  }

  @GetMapping({"", "/"})
  @PreAuthorize("hasAuthority('sales:read')")
  public DataPage<SellPojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    return super.readMany(null, null, allRequestParams);
  }

  @Override
  @PostMapping({"", "/"})
  @PreAuthorize("hasAuthority('sales:create')")
  public void create(@Valid @RequestBody SellPojo input) throws BadInputException, EntityAlreadyExistsException {
    crudService.create(input);
  }

  @Override
  @GetMapping({"/{buyOrder}", "/{buyOrder}/"})
  @PreAuthorize("hasAuthority('sales:read')")
  public SellPojo readOne(@PathVariable Long buyOrder) throws NotFoundException {
    return crudService.readOne(buyOrder);
  }

  @Override
  @PutMapping({"/{buyOrder}", "/{buyOrder}/"})
  @PreAuthorize("hasAuthority('sales:update')")
  public void update(@RequestBody SellPojo input, @PathVariable Long buyOrder)
    throws BadInputException, NotFoundException {
    crudService.update(input, buyOrder);
  }

  @Override
  @DeleteMapping({"/{buyOrder}", "/{buyOrder}/"})
  @PreAuthorize("hasAuthority('sales:delete')")
  public void delete(@PathVariable Long buyOrder) throws NotFoundException {
    crudService.delete(buyOrder);
  }
}
