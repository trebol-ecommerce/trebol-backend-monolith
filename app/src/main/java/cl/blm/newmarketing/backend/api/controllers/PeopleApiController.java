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
import cl.blm.newmarketing.backend.api.DtoCrudServiceClient;
import cl.blm.newmarketing.backend.model.entities.Person;
import cl.blm.newmarketing.backend.services.impl.GenericCrudService;

/**
 * API point of entry for Person entities
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/api")
public class PeopleApiController
    extends DtoCrudServiceClient<Person, Integer> {
  private final static Logger LOG = LoggerFactory.getLogger(PeopleApiController.class);

  @Autowired
  public PeopleApiController(CustomProperties globals, GenericCrudService<Person, Integer> crudService) {
    super(globals, crudService);
  }

  @GetMapping("/people")
  public Collection<Person> read(@RequestParam Map<String, String> allRequestParams) {
    return this.read(null, null, allRequestParams);
  }

  @GetMapping("/people/{requestPageSize}")
  public Collection<Person> read(@PathVariable Integer requestPageSize,
      @RequestParam Map<String, String> allRequestParams) {
    return this.read(requestPageSize, null, allRequestParams);
  }

  @GetMapping("/people/{requestPageSize}/{requestPageIndex}")
  public Collection<Person> read(@PathVariable Integer requestPageSize, @PathVariable Integer requestPageIndex,
      @RequestParam Map<String, String> allRequestParams) {
    LOG.info("read");
    Collection<Person> people = this.readFromService(requestPageSize, requestPageIndex, allRequestParams);
    return people;
  }
}
