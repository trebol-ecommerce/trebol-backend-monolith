package cl.blm.newmarketing.backend.api.controllers;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.blm.newmarketing.backend.CustomProperties;
import cl.blm.newmarketing.backend.api.PojosApiController;
import cl.blm.newmarketing.backend.api.DtoCrudServiceClient;
import cl.blm.newmarketing.backend.api.pojos.ClientPojo;
import cl.blm.newmarketing.backend.dtos.ClientDto;
import cl.blm.newmarketing.backend.services.DtoCrudService;

/**
 * API point of entry for Client entities
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/api")
public class ClientsApiController
    extends DtoCrudServiceClient<ClientDto, Integer>
    implements PojosApiController<ClientPojo, Integer> {
  private final static Logger LOG = LoggerFactory.getLogger(ClientsApiController.class);

  @Autowired
  private ConversionService conversion;

  @SuppressWarnings("unchecked")
  private List<ClientPojo> convertCollection(Collection<ClientDto> source) {
    return (List<ClientPojo>) conversion.convert(source,
        TypeDescriptor.collection(Collection.class, TypeDescriptor.valueOf(ClientDto.class)),
        TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(ClientPojo.class)));
  }

  @Autowired
  public ClientsApiController(CustomProperties globals, DtoCrudService<ClientDto, Integer> crudService) {
    super(globals, crudService);
  }

  @PostMapping("/client")
  public ClientPojo create(@RequestBody ClientPojo input) {
    LOG.info("create");
    ClientDto dto = conversion.convert(input, ClientDto.class);
    ClientDto processed = crudService.create(dto);
    ClientPojo result = conversion.convert(processed, ClientPojo.class);
    return result;
  }

  @GetMapping("/client/{id}")
  public ClientPojo readOne(@PathVariable Integer id) {
    LOG.info("read");
    ClientDto foundClient = crudService.find(id);
    if (foundClient == null) {
      return null;
    } else {
      ClientPojo result = conversion.convert(foundClient, ClientPojo.class);
      return result;
    }
  }

  @GetMapping("/clients")
  public Collection<ClientPojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    return this.readMany(null, null, allRequestParams);
  }

  @GetMapping("/clients/{requestPageSize}")
  public Collection<ClientPojo> readMany(@PathVariable Integer requestPageSize,
      @RequestParam Map<String, String> allRequestParams) {
    return this.readMany(requestPageSize, null, allRequestParams);
  }

  @GetMapping("/clients/{requestPageSize}/{requestPageIndex}")
  public Collection<ClientPojo> readMany(@PathVariable Integer requestPageSize, @PathVariable Integer requestPageIndex,
      @RequestParam Map<String, String> allRequestParams) {
    LOG.info("read");
    Collection<ClientDto> clients = this.readFromService(requestPageSize, requestPageIndex, allRequestParams);
    List<ClientPojo> clientPojos = this.convertCollection(clients);
    return clientPojos;
  }

  @PutMapping("/client")
  public ClientPojo update(@RequestBody ClientPojo input) {
    LOG.info("update");
    ClientDto dto = conversion.convert(input, ClientDto.class);
    ClientDto processed = crudService.update(dto);
    ClientPojo result = conversion.convert(processed, ClientPojo.class);
    return result;
  }

  @PutMapping("/client/{id}")
  public ClientPojo update(@RequestBody ClientPojo input, @PathVariable Integer id) {
    LOG.info("update");
    ClientDto dto = conversion.convert(input, ClientDto.class);
    ClientDto processed = crudService.update(dto, id);
    ClientPojo result = conversion.convert(processed, ClientPojo.class);
    return result;
  }

  @DeleteMapping("/client/{id}")
  public boolean delete(@PathVariable Integer id) {
    LOG.info("delete");
    boolean result = crudService.delete(id);
    return result;
  }
}
