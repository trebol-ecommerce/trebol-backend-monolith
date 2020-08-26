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

import com.querydsl.core.types.Predicate;

import cl.blm.newmarketing.backend.BackendAppGlobals;
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
public class PeopleController {
  private final static Logger LOG = LoggerFactory.getLogger(PeopleController.class);

  @Autowired
  private ConversionService conversion;
  @Autowired
  private CrudService<PersonDto, Integer> personSvc;
  @Autowired
  private BackendAppGlobals globals;

  @GetMapping("/people")
  public Collection<PersonPojo> read(@RequestParam Map<String, String> allRequestParams) {
    return this.read(null, null, allRequestParams);
  }

  @GetMapping("/people/{requestPageSize}")
  public Collection<PersonPojo> read(@PathVariable Integer requestPageSize,
      @RequestParam Map<String, String> allRequestParams) {
    return this.read(requestPageSize, null, allRequestParams);
  }

  /**
   * Retrieve a page of people.
   *
   * @param requestPageSize
   * @param requestPageIndex
   * @param allRequestParams
   *
   * @see RequestParam
   * @see Predicate
   * @return
   */
  @GetMapping("/people/{requestPageSize}/{requestPageIndex}")
  public Collection<PersonPojo> read(@PathVariable Integer requestPageSize, @PathVariable Integer requestPageIndex,
      @RequestParam Map<String, String> allRequestParams) {
    LOG.info("read");
    Integer pageSize = globals.ITEMS_PER_PAGE;
    Integer pageIndex = 0;
    Predicate filters = null;

    if (requestPageSize != null && requestPageSize > 0) {
      pageSize = requestPageSize;
    }
    if (requestPageIndex != null && requestPageIndex > 0) {
      pageIndex = requestPageIndex - 1;
    }
    if (allRequestParams != null && !allRequestParams.isEmpty()) {
      filters = personSvc.queryParamsMapToPredicate(allRequestParams);
    }

    Collection<PersonDto> people = personSvc.read(pageSize, pageIndex, filters);
    Collection<PersonPojo> peoplePojos = (List<PersonPojo>) conversion.convert(people,
        TypeDescriptor.collection(Collection.class, TypeDescriptor.valueOf(PersonDto.class)),
        TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(PersonPojo.class)));
    return peoplePojos;
  }
}
