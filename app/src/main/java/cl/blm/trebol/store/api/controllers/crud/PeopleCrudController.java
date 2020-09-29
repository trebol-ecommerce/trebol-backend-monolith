package cl.blm.trebol.store.api.controllers.crud;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.blm.trebol.store.api.GenericEntityDataController;
import cl.blm.trebol.store.api.pojo.PersonPojo;
import cl.blm.trebol.store.config.CustomProperties;
import cl.blm.trebol.store.jpa.entities.Person;
import cl.blm.trebol.store.services.crud.GenericEntityCrudService;

/**
 * API point of entry for Person entities
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/api")
public class PeopleCrudController
    extends GenericEntityDataController<PersonPojo, Person, Integer> {

  @Autowired
  public PeopleCrudController(CustomProperties globals,
      GenericEntityCrudService<PersonPojo, Person, Integer> crudService) {
    super(globals, crudService);
  }

  @GetMapping("/people")
  @PreAuthorize("hasAuthority('people:read')")
  public Collection<PersonPojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    return super.readMany(null, null, allRequestParams);
  }

  @GetMapping("/people/{requestPageSize}")
  @PreAuthorize("hasAuthority('people:read')")
  public Collection<PersonPojo> readMany(@PathVariable Integer requestPageSize,
      @RequestParam Map<String, String> allRequestParams) {
    return super.readMany(requestPageSize, null, allRequestParams);
  }

  @Override
  @GetMapping("/people/{requestPageSize}/{requestPageIndex}")
  @PreAuthorize("hasAuthority('people:read')")
  public Collection<PersonPojo> readMany(@PathVariable Integer requestPageSize, @PathVariable Integer requestPageIndex,
      @RequestParam Map<String, String> allRequestParams) {
    return super.readMany(requestPageSize, requestPageIndex, allRequestParams);
  }
}
