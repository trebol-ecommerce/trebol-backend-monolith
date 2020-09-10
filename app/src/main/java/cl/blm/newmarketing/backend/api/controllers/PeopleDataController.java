package cl.blm.newmarketing.backend.api.controllers;

import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.blm.newmarketing.backend.CustomProperties;
import cl.blm.newmarketing.backend.api.GenericEntityDataController;
import cl.blm.newmarketing.backend.api.pojo.PersonPojo;
import cl.blm.newmarketing.backend.jpa.entities.Person;
import cl.blm.newmarketing.backend.services.data.GenericEntityDataService;

/**
 * API point of entry for Person entities
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/api")
public class PeopleDataController
    extends GenericEntityDataController<PersonPojo, Person, Integer> {
  private final static Logger LOG = LoggerFactory.getLogger(PeopleDataController.class);

  @Autowired
  public PeopleDataController(CustomProperties globals, GenericEntityDataService<PersonPojo, Person, Integer> crudService) {
    super(LOG, globals, crudService);
  }

  @GetMapping("/people")
  public Collection<PersonPojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    return this.readMany(null, null, allRequestParams);
  }

  @GetMapping("/people/{requestPageSize}")
  public Collection<PersonPojo> readMany(@PathVariable Integer requestPageSize,
      @RequestParam Map<String, String> allRequestParams) {
    return this.readMany(requestPageSize, null, allRequestParams);
  }

  @Override
  @GetMapping("/people/{requestPageSize}/{requestPageIndex}")
  public Collection<PersonPojo> readMany(@PathVariable Integer requestPageSize, @PathVariable Integer requestPageIndex,
      @RequestParam Map<String, String> allRequestParams) {
    return super.readMany(requestPageSize, requestPageIndex, allRequestParams);
  }
}
