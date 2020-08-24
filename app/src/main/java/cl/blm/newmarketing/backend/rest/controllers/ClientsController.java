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
public class ClientsController
    extends EntityCrudController<ClientDto, Integer> {
  private final static Logger LOG = LoggerFactory.getLogger(ClientsController.class);

  @Autowired
  private ConversionService conversion;

  @SuppressWarnings("unchecked")
  private List<ClientPojo> convertCollection(Collection<ClientDto> source) {
    return (List<ClientPojo>) conversion.convert(source,
        TypeDescriptor.collection(Collection.class, TypeDescriptor.valueOf(ClientDto.class)),
        TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(ClientPojo.class)));
  }

  @Autowired
  public ClientsController(BackendAppGlobals globals, CrudService<ClientDto, Integer> crudService) {
    super(globals, crudService);
  }

  @GetMapping("/clients")
  public Collection<ClientPojo> read(@RequestParam Map<String, String> allRequestParams) {
    return this.read(null, null, allRequestParams);
  }

  @GetMapping("/clients/{requestPageSize}")
  public Collection<ClientPojo> read(@PathVariable Integer requestPageSize,
      @RequestParam Map<String, String> allRequestParams) {
    return this.read(requestPageSize, null, allRequestParams);
  }

  @GetMapping("/clients/{requestPageSize}/{requestPageIndex}")
  public Collection<ClientPojo> read(@PathVariable Integer requestPageSize, @PathVariable Integer requestPageIndex,
      @RequestParam Map<String, String> allRequestParams) {
    LOG.info("read");
    Collection<ClientDto> clients = this.readFromService(requestPageSize, requestPageIndex, allRequestParams);
    List<ClientPojo> clientPojos = this.convertCollection(clients);
    return clientPojos;
  }
}
