package org.trebol.operation.controllers;

import com.querydsl.core.types.Predicate;
import io.jsonwebtoken.lang.Maps;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.trebol.config.OperationProperties;
import org.trebol.exceptions.BadInputException;
import org.trebol.exceptions.EntityAlreadyExistsException;
import org.trebol.jpa.entities.Sell;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.IPredicateJpaService;
import org.trebol.operation.GenericDataCrudController;
import org.trebol.pojo.DataPagePojo;
import org.trebol.pojo.SellPojo;

import javax.validation.Valid;
import java.util.Map;

/**
 * Controller that maps API resources for CRUD operations on Sales
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/data/sales")
@PreAuthorize("isAuthenticated()")
public class DataSalesController
  extends GenericDataCrudController<SellPojo, Sell> {

  @Autowired
  public DataSalesController(OperationProperties globals,
                             GenericCrudJpaService<SellPojo, Sell> crudService,
                             IPredicateJpaService<Sell> predicateService) {
    super(globals, crudService, predicateService);
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
    super.create(input);
  }

  @Override
  @PutMapping({"", "/"})
  @PreAuthorize("hasAuthority('sales:update')")
  public void update(@RequestBody SellPojo input, @RequestParam Map<String, String> requestParams)
      throws BadInputException, NotFoundException {
    super.update(input, requestParams);
  }

  @Override
  @DeleteMapping({"", "/"})
  @PreAuthorize("hasAuthority('sales:delete')")
  public void delete(@RequestParam Map<String, String> requestParams) throws NotFoundException {
    super.delete(requestParams);
  }

  @Deprecated(forRemoval = true)
  @GetMapping({"/{buyOrder}", "/{buyOrder}/"})
  @PreAuthorize("hasAuthority('sales:read')")
  public SellPojo readOne(@PathVariable Long buyOrder) throws NotFoundException {
    return crudService.readOne(whereBuyOrderIs(buyOrder));
  }

  @Deprecated(forRemoval = true)
  @PutMapping({"/{buyOrder}", "/{buyOrder}/"})
  @PreAuthorize("hasAuthority('sales:update')")
  public void update(@RequestBody SellPojo input, @PathVariable Long buyOrder)
    throws BadInputException, NotFoundException {
    crudService.update(input, whereBuyOrderIs(buyOrder));
  }

  @Deprecated(forRemoval = true)
  @DeleteMapping({"/{buyOrder}", "/{buyOrder}/"})
  @PreAuthorize("hasAuthority('sales:delete')")
  public void delete(@PathVariable Long buyOrder) throws NotFoundException {
    crudService.delete(whereBuyOrderIs(buyOrder));
  }

  private Predicate whereBuyOrderIs(Long buyOrder) {
    Map<String, String> buyOrderMatcher = Maps.of("buyOrder", String.valueOf(buyOrder)).build();
    return predicateService.parseMap(buyOrderMatcher);
  }
}
