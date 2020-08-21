package cl.blm.newmarketing.backend.rest.controllers;

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
import cl.blm.newmarketing.backend.pojos.ClientPojo;
import cl.blm.newmarketing.backend.rest.dtos.ClientDto;
import cl.blm.newmarketing.backend.rest.services.CrudService;

/**
 * API point of entry for Client entities
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/api")
public class ClientsController {
  private final static Logger LOG = LoggerFactory.getLogger(ClientsController.class);

  @Autowired
  private ConversionService conversion;
  @Autowired
  private CrudService<ClientDto, Integer> clientSvc;
  @Autowired
  private BackendAppGlobals globals;

  @GetMapping("/clients")
  public Collection<ClientPojo> read(@RequestParam Map<String, String> allRequestParams) {
    return this.read(null, null, allRequestParams);
  }

  @GetMapping("/clients/{requestPageSize}")
  public Collection<ClientPojo> read(@PathVariable Integer requestPageSize,
      @RequestParam Map<String, String> allRequestParams) {
    return this.read(requestPageSize, null, allRequestParams);
  }

  /**
   * Retrieve a page of clients.
   *
   * @param requestPageSize
   * @param requestPageIndex
   * @param allRequestParams
   *
   * @see RequestParam
   * @see Predicate
   * @return
   */
  @GetMapping("/clients/{requestPageSize}/{requestPageIndex}")
  public Collection<ClientPojo> read(@PathVariable Integer requestPageSize, @PathVariable Integer requestPageIndex,
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
      filters = clientSvc.queryParamsMapToPredicate(allRequestParams);
    }

    Collection<ClientDto> clients = clientSvc.read(pageSize, pageIndex, filters);
    Collection<ClientPojo> clientPojos = (List<ClientPojo>) conversion.convert(clients,
        TypeDescriptor.collection(Collection.class, TypeDescriptor.valueOf(ClientDto.class)),
        TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(ClientPojo.class)));
    return clientPojos;
  }
}
