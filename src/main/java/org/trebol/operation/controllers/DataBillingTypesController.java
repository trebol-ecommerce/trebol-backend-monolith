package org.trebol.operation.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.trebol.config.OperationProperties;
import org.trebol.jpa.entities.BillingType;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.IPredicateJpaService;
import org.trebol.operation.GenericDataController;
import org.trebol.pojo.BillingTypePojo;
import org.trebol.pojo.DataPagePojo;

import java.util.Map;

/**
 * Controller that maps API resource to read existing BillingTypes
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/data/billing_types")
public class DataBillingTypesController
  extends GenericDataController<BillingTypePojo, BillingType> {

  @Autowired
  public DataBillingTypesController(OperationProperties globals,
                                    GenericCrudJpaService<BillingTypePojo, BillingType> crudService,
                                    IPredicateJpaService<BillingType> predicateService) {
    super(globals, crudService, predicateService);
  }

  @GetMapping({"", "/"})
  public DataPagePojo<BillingTypePojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    return super.readMany(null, null, allRequestParams);
  }
}
