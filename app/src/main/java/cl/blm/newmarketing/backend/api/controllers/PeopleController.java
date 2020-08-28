package cl.blm.newmarketing.backend.api.controllers;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.blm.newmarketing.backend.BackendAppGlobals;
import cl.blm.newmarketing.backend.api.CrudServiceClient;
import cl.blm.newmarketing.backend.api.pojos.PersonPojo;
import cl.blm.newmarketing.backend.dtos.PersonDto;
import cl.blm.newmarketing.backend.services.CrudService;

/**
 * API point of entry for Person entities
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/api")
public class PeopleController
    extends CrudServiceClient<PersonDto, Integer> {
  private final static Logger LOG = LoggerFactory.getLogger(PeopleController.class);

  @Autowired
  private ConversionService conversion;

  @SuppressWarnings("unchecked")
  private List<PersonPojo> convertCollection(Collection<PersonDto> source) {
    return (List<PersonPojo>) conversion.convert(source,
        TypeDescriptor.collection(Collection.class, TypeDescriptor.valueOf(PersonDto.class)),
        TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(PersonPojo.class)));
  }

  @Autowired
  public PeopleController(BackendAppGlobals globals, CrudService<PersonDto, Integer> crudService) {
    super(globals, crudService);
  }

  @GetMapping("/people")
  public Collection<PersonPojo> read(@RequestParam Map<String, String> allRequestParams) {
    return this.read(null, null, allRequestParams);
  }

  @GetMapping("/people/{requestPageSize}")
  public Collection<PersonPojo> read(@PathVariable Integer requestPageSize,
      @RequestParam Map<String, String> allRequestParams) {
    return this.read(requestPageSize, null, allRequestParams);
  }

  @GetMapping("/people/{requestPageSize}/{requestPageIndex}")
  public Collection<PersonPojo> read(@PathVariable Integer requestPageSize, @PathVariable Integer requestPageIndex,
      @RequestParam Map<String, String> allRequestParams) {
    LOG.info("read");
    Collection<PersonDto> people = this.readFromService(requestPageSize, requestPageIndex, allRequestParams);
    Collection<PersonPojo> peoplePojos = this.convertCollection(people);
    return peoplePojos;
  }
}
