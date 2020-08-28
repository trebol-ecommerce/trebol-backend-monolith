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
import cl.blm.newmarketing.backend.api.DtoCrudServiceClient;
import cl.blm.newmarketing.backend.api.ApiCrudController;
import cl.blm.newmarketing.backend.api.pojos.SellPojo;
import cl.blm.newmarketing.backend.dtos.SellDto;
import cl.blm.newmarketing.backend.services.DtoCrudService;

/**
 * API point of entry for Sell entities
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/api")
public class SalesApiController
    extends DtoCrudServiceClient<SellDto, Integer>
    implements ApiCrudController<SellPojo, Integer> {
  private final static Logger LOG = LoggerFactory.getLogger(SalesApiController.class);

  @Autowired
  private ConversionService conversion;

  @SuppressWarnings("unchecked")
  private List<SellPojo> convertCollection(Collection<SellDto> source) {
    return (List<SellPojo>) conversion.convert(source,
        TypeDescriptor.collection(Collection.class, TypeDescriptor.valueOf(SellDto.class)),
        TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(SellPojo.class)));
  }

  @Autowired
  public SalesApiController(CustomProperties globals, DtoCrudService<SellDto, Integer> crudService) {
    super(globals, crudService);
  }

  @PostMapping("/sell")
  public SellPojo create(@RequestBody SellPojo input) {
    LOG.info("create");
    SellDto dto = conversion.convert(input, SellDto.class);
    SellDto processed = crudService.create(dto);
    SellPojo result = conversion.convert(processed, SellPojo.class);
    return result;
  }

  @GetMapping("/sell/{id}")
  public SellPojo readOne(@PathVariable Integer id) {
    LOG.info("read");
    SellDto foundClient = crudService.find(id);
    if (foundClient == null) {
      return null;
    } else {
      SellPojo result = conversion.convert(foundClient, SellPojo.class);
      return result;
    }
  }

  @GetMapping("/sales")
  public Collection<SellPojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    return this.readMany(null, null, allRequestParams);
  }

  @GetMapping("/sales/{requestPageSize}")
  public Collection<SellPojo> readMany(@PathVariable Integer requestPageSize,
      @RequestParam Map<String, String> allRequestParams) {
    return this.readMany(requestPageSize, null, allRequestParams);
  }

  @GetMapping("/sales/{requestPageSize}/{requestPageIndex}")
  public Collection<SellPojo> readMany(@PathVariable Integer requestPageSize, @PathVariable Integer requestPageIndex,
      @RequestParam Map<String, String> allRequestParams) {
    LOG.info("read");
    Collection<SellDto> sales = this.readFromService(requestPageSize, requestPageIndex, allRequestParams);
    Collection<SellPojo> sellPojos = this.convertCollection(sales);
    return sellPojos;
  }

  @PutMapping("/sell")
  public SellPojo update(@RequestBody SellPojo input) {
    LOG.info("update");
    SellDto dto = conversion.convert(input, SellDto.class);
    SellDto processed = crudService.update(dto);
    SellPojo result = conversion.convert(processed, SellPojo.class);
    return result;
  }

  @PutMapping("/sell/{id}")
  public SellPojo update(@RequestBody SellPojo input, @PathVariable Integer id) {
    LOG.info("update");
    SellDto dto = conversion.convert(input, SellDto.class);
    SellDto processed = crudService.update(dto, id);
    SellPojo result = conversion.convert(processed, SellPojo.class);
    return result;
  }

  @DeleteMapping("/sell/{id}")
  public boolean delete(@PathVariable Integer id) {
    LOG.info("delete");
    boolean result = crudService.delete(id);
    return result;
  }
}
