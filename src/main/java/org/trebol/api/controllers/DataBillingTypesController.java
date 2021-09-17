package org.trebol.api.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.trebol.api.GenericDataController;
import org.trebol.api.DataPage;
import org.trebol.pojo.BillingTypePojo;
import org.trebol.config.CustomProperties;
import org.trebol.jpa.GenericJpaCrudService;
import org.trebol.jpa.entities.BillingType;

/**
 * API point of entry for ProductCategory entities
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/data/billing_types")
public class DataBillingTypesController
  extends GenericDataController<BillingTypePojo, BillingType> {

  @Autowired
  public DataBillingTypesController(CustomProperties globals,
    GenericJpaCrudService<BillingTypePojo, BillingType> crudService) {
    super(globals, crudService);
  }

  @GetMapping({"", "/"})
  public DataPage<BillingTypePojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    return super.readMany(null, null, allRequestParams);
  }
}
