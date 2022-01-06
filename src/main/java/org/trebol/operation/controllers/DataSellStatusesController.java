package org.trebol.operation.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.trebol.config.OperationProperties;
import org.trebol.jpa.entities.SellStatus;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.IPredicateJpaService;
import org.trebol.operation.GenericDataController;
import org.trebol.pojo.DataPagePojo;
import org.trebol.pojo.SellStatusPojo;

import java.util.Map;

/**
 * Controller that maps API resource to read existing SellStatuses
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/data/sell_statuses")
@PreAuthorize("isAuthenticated()")
public class DataSellStatusesController
  extends GenericDataController<SellStatusPojo, SellStatus> {

  @Autowired
  public DataSellStatusesController(OperationProperties globals,
                                    GenericCrudJpaService<SellStatusPojo, SellStatus> crudService,
                                    IPredicateJpaService<SellStatus> predicateService) {
    super(globals, crudService, predicateService);
  }

  @GetMapping({"", "/"})
  @PreAuthorize("hasAuthority('sell_statuses:read')")
  public DataPagePojo<SellStatusPojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    return super.readMany(allRequestParams);
  }
}
