package org.trebol.operation.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.trebol.pojo.DataPagePojo;
import org.trebol.operation.GenericDataController;
import org.trebol.pojo.PersonPojo;
import org.trebol.config.OperationProperties;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.GenericJpaService;

/**
 * Controller that maps API resource to read existing People
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/data/people")
@PreAuthorize("isAuthenticated()")
public class DataPeopleController
  extends GenericDataController<PersonPojo, Person> {

  @Autowired
  public DataPeopleController(OperationProperties globals,
                              GenericJpaService<PersonPojo, Person> crudService) {
    super(globals, crudService);
  }

  @GetMapping({"", "/"})
  @PreAuthorize("hasAuthority('people:read')")
  public DataPagePojo<PersonPojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    return super.readMany(null, null, allRequestParams);
  }
}
