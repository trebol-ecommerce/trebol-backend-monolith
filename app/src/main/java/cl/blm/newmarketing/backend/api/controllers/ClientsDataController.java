package cl.blm.newmarketing.backend.api.controllers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import cl.blm.newmarketing.backend.CustomProperties;
import cl.blm.newmarketing.backend.api.GenericDataController;
import cl.blm.newmarketing.backend.api.DtoCrudServiceClient;
import cl.blm.newmarketing.backend.model.entities.Client;
import cl.blm.newmarketing.backend.services.data.GenericDataService;

/**
 * API point of entry for Client entities
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/api")
public class ClientsDataController
    extends DtoCrudServiceClient<Client, Integer>
    implements GenericDataController<Client, Integer> {
  private final static Logger LOG = LoggerFactory.getLogger(ClientsDataController.class);

  @Autowired
  public ClientsDataController(CustomProperties globals, GenericDataService<Client, Integer> crudService) {
    super(globals, crudService);
  }

  @PostMapping("/client")
  public Client create(@RequestBody @Valid Client input) {
    LOG.info("create");
    Client result = crudService.create(input);
    return result;
  }

  @GetMapping("/client/{id}")
  public Client readOne(@PathVariable Integer id) {
    LOG.info("read");
    Client found = crudService.find(id);
    return found;
  }

  @GetMapping("/clients")
  public Collection<Client> readMany(@RequestParam Map<String, String> allRequestParams) {
    return this.readMany(null, null, allRequestParams);
  }

  @GetMapping("/clients/{requestPageSize}")
  public Collection<Client> readMany(@PathVariable Integer requestPageSize,
      @RequestParam Map<String, String> allRequestParams) {
    return this.readMany(requestPageSize, null, allRequestParams);
  }

  @GetMapping("/clients/{requestPageSize}/{requestPageIndex}")
  public Collection<Client> readMany(@PathVariable Integer requestPageSize, @PathVariable Integer requestPageIndex,
      @RequestParam Map<String, String> allRequestParams) {
    LOG.info("read");
    Collection<Client> clients = this.readFromService(requestPageSize, requestPageIndex, allRequestParams);
    LOG.info("{}", clients);
    return clients;
  }

  @PutMapping("/client")
  public Client update(@RequestBody @Valid Client input) {
    LOG.info("update");
    Client processed = crudService.update(input, input.getId());
    return processed;
  }

  @PutMapping("/client/{id}")
  public Client update(@RequestBody @Valid Client input, @PathVariable Integer id) {
    LOG.info("update");
    Client processed = crudService.update(input, id);
    return processed;
  }

  @DeleteMapping("/client/{id}")
  public boolean delete(@PathVariable Integer id) {
    LOG.info("delete");
    boolean result = crudService.delete(id);
    return result;
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    return errors;
  }
}
