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
import org.trebol.jpa.GenericJpaService;
import org.trebol.jpa.entities.Shipper;
import org.trebol.operation.GenericDataController;
import org.trebol.operation.GenericDataCrudController;
import org.trebol.operation.IDataCrudController;
import org.trebol.pojo.DataPagePojo;
import org.trebol.pojo.ShipperPojo;

import javax.validation.Valid;
import java.util.Map;

/**
 * Controller that maps API resources for CRUD operations on Shippers
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/data/shippers")
public class DataShippersController
  extends GenericDataCrudController<ShipperPojo, Shipper> {

  @Autowired
  public DataShippersController(OperationProperties globals,
                                GenericJpaService<ShipperPojo, Shipper> crudService) {
    super(globals, crudService);
  }

  @GetMapping({"", "/"})
  public DataPagePojo<ShipperPojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    return super.readMany(null, null, allRequestParams);
  }

  @Override
  @PostMapping({"", "/"})
  @PreAuthorize("hasAuthority('shippers:create')")
  public void create(@Valid @RequestBody ShipperPojo input) throws BadInputException, EntityAlreadyExistsException {
    super.create(input);
  }

  @Override
  @PutMapping({"", "/"})
  @PreAuthorize("hasAuthority('shippers:update')")
  public void update(@RequestBody ShipperPojo input, @RequestParam Map<String, String> requestParams)
      throws BadInputException, NotFoundException {
    super.update(input, requestParams);
  }

  @Override
  @DeleteMapping({"", "/"})
  @PreAuthorize("hasAuthority('shippers:delete')")
  public void delete(@RequestParam Map<String, String> requestParams) throws NotFoundException {
    super.delete(requestParams);
  }
}
