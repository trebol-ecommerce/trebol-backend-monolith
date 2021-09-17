package org.trebol.api.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.trebol.api.DataPage;
import org.trebol.api.GenericDataController;
import org.trebol.pojo.SellStatusPojo;
import org.trebol.config.CustomProperties;
import org.trebol.jpa.entities.SellStatus;
import org.trebol.jpa.GenericJpaCrudService;

/**
 * API point of entry for SellStatus entities
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/data/sell_statuses")
public class DataSellStatusesController
  extends GenericDataController<SellStatusPojo, SellStatus> {

  @Autowired
  public DataSellStatusesController(CustomProperties globals,
      GenericJpaCrudService<SellStatusPojo, SellStatus> crudService) {
    super(globals, crudService);
  }

  @GetMapping({"", "/"})
  @PreAuthorize("hasAuthority('sell_statuses:read')")
  public DataPage<SellStatusPojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    return super.readMany(null, null, allRequestParams);
  }
}
