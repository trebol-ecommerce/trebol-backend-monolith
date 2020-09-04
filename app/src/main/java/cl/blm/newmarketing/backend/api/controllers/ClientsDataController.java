package cl.blm.newmarketing.backend.api.controllers;

import java.util.Collection;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import cl.blm.newmarketing.backend.api.GenericEntityQueryController;
import cl.blm.newmarketing.backend.api.pojo.ClientPojo;
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
    extends GenericEntityQueryController<ClientPojo, Client, Integer> {
  private final static Logger LOG = LoggerFactory.getLogger(ClientsDataController.class);

  @Autowired
  public ClientsDataController(CustomProperties globals, GenericDataService<ClientPojo, Client, Integer> crudService) {
    super(LOG, globals, crudService);
  }

  @Override
  @PostMapping("/client")
  public Integer create(@RequestBody @Valid ClientPojo input) {
    return super.create(input);
  }

  @Override
  @GetMapping("/client/{id}")
  public ClientPojo readOne(@PathVariable Integer id) {
    return super.readOne(id);
  }

  @GetMapping("/clients")
  public Collection<ClientPojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    return super.readMany(null, null, allRequestParams);
  }

  @GetMapping("/clients/{requestPageSize}")
  public Collection<ClientPojo> readMany(@PathVariable Integer requestPageSize,
      @RequestParam Map<String, String> allRequestParams) {
    return super.readMany(requestPageSize, null, allRequestParams);
  }

  @Override
  @GetMapping("/clients/{requestPageSize}/{requestPageIndex}")
  public Collection<ClientPojo> readMany(@PathVariable Integer requestPageSize, @PathVariable Integer requestPageIndex,
      @RequestParam Map<String, String> allRequestParams) {
    return super.readMany(requestPageSize, requestPageIndex, allRequestParams);
  }

  @PutMapping("/client")
  public Integer update(@RequestBody @Valid ClientPojo input) {
    return super.update(input, input.getId());
  }

  @Override
  @PutMapping("/client/{id}")
  public Integer update(@RequestBody @Valid ClientPojo input, @PathVariable Integer id) {
    return super.update(input, id);
  }

  @Override
  @DeleteMapping("/client/{id}")
  public boolean delete(@PathVariable Integer id) {
    return super.delete(id);
  }

  @Override
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
    return super.handleValidationExceptions(ex);
  }
}
