package org.trebol.operation.controllers;

import java.util.Map;

import javax.validation.Valid;

import com.querydsl.core.types.Predicate;
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

import org.trebol.operation.GenericDataController;
import org.trebol.pojo.DataPagePojo;
import org.trebol.pojo.ProductPojo;
import org.trebol.pojo.SellPojo;
import org.trebol.config.OperationProperties;
import org.trebol.jpa.entities.Sell;
import org.trebol.exceptions.EntityAlreadyExistsException;
import org.trebol.jpa.GenericJpaService;

import javassist.NotFoundException;

import org.trebol.operation.IDataCrudController;
import org.trebol.exceptions.BadInputException;

/**
 * Controller that maps API resources for CRUD operations on Sales
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/data/sales")
@PreAuthorize("isAuthenticated()")
public class DataSalesController
  extends GenericDataController<SellPojo, Sell>
  implements IDataCrudController<SellPojo, Long> {

  @Autowired
  public DataSalesController(OperationProperties globals, GenericJpaService<SellPojo, Sell> crudService) {
    super(globals, crudService);
  }

  @GetMapping({"", "/"})
  @PreAuthorize("hasAuthority('sales:read')")
  public DataPagePojo<SellPojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    return super.readMany(null, null, allRequestParams);
  }

  @Override
  @PostMapping({"", "/"})
  @PreAuthorize("hasAuthority('sales:create')")
  public void create(@Valid @RequestBody SellPojo input) throws BadInputException, EntityAlreadyExistsException {
    crudService.create(input);
  }

  @Override
  @PutMapping({"", "/"})
  @PreAuthorize("hasAuthority('sales:update')")
  public void update(@RequestBody SellPojo input, @RequestParam Map<String, String> requestParams)
      throws BadInputException, NotFoundException {
    if (!requestParams.isEmpty()) {
      Predicate predicate = crudService.parsePredicate(requestParams);
      SellPojo existing = crudService.readOne(predicate);
      crudService.update(input, existing.getBuyOrder());
    } else {
      crudService.update(input);
    }
  }

  @Override
  @DeleteMapping({"", "/"})
  @PreAuthorize("hasAuthority('sales:delete')")
  public void delete(@RequestParam Map<String, String> requestParams) throws NotFoundException {
    Predicate predicate = crudService.parsePredicate(requestParams);
    crudService.delete(predicate);
  }

  @Deprecated
  @GetMapping({"/{buyOrder}", "/{buyOrder}/"})
  @PreAuthorize("hasAuthority('sales:read')")
  public SellPojo readOne(@PathVariable Long buyOrder) throws NotFoundException {
    return crudService.readOne(buyOrder);
  }

  @Deprecated
  @PutMapping({"/{buyOrder}", "/{buyOrder}/"})
  @PreAuthorize("hasAuthority('sales:update')")
  public void update(@RequestBody SellPojo input, @PathVariable Long buyOrder)
    throws BadInputException, NotFoundException {
    crudService.update(input, buyOrder);
  }

  @Deprecated
  @DeleteMapping({"/{buyOrder}", "/{buyOrder}/"})
  @PreAuthorize("hasAuthority('sales:delete')")
  public void delete(@PathVariable Long buyOrder) throws NotFoundException {
    crudService.delete(buyOrder);
  }
}
