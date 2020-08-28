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
import cl.blm.newmarketing.backend.api.pojos.ProductPojo;
import cl.blm.newmarketing.backend.dtos.ProductDto;
import cl.blm.newmarketing.backend.services.DtoCrudService;

/**
 * API point of entry for Product entities
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/api")
public class ProductsApiController
    extends DtoCrudServiceClient<ProductDto, Integer>
    implements PojosApiController<ProductPojo, Integer> {
  private final static Logger LOG = LoggerFactory.getLogger(ProductsApiController.class);

  @Autowired
  private ConversionService conversion;

  @SuppressWarnings("unchecked")
  private List<ProductPojo> convertCollection(Collection<ProductDto> source) {
    return (List<ProductPojo>) conversion.convert(source,
        TypeDescriptor.collection(Collection.class, TypeDescriptor.valueOf(ProductDto.class)),
        TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(ProductPojo.class)));
  }

  @Autowired
  public ProductsApiController(CustomProperties globals, DtoCrudService<ProductDto, Integer> crudService) {
    super(globals, crudService);
  }

  @PostMapping("/product")
  public ProductPojo create(@RequestBody ProductPojo input) {
    LOG.info("create");
    ProductDto dto = conversion.convert(input, ProductDto.class);
    ProductDto processed = crudService.create(dto);
    ProductPojo result = conversion.convert(processed, ProductPojo.class);
    return result;
  }

  @GetMapping("/product/{id}")
  public ProductPojo readOne(@PathVariable Integer id) {
    LOG.info("read");
    ProductDto foundClient = crudService.find(id);
    if (foundClient == null) {
      return null;
    } else {
      ProductPojo result = conversion.convert(foundClient, ProductPojo.class);
      return result;
    }
  }

  @GetMapping("/products")
  public Collection<ProductPojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    return this.readMany(null, null, allRequestParams);
  }

  @GetMapping("/products/{requestPageSize}")
  public Collection<ProductPojo> readMany(@PathVariable Integer requestPageSize,
      @RequestParam Map<String, String> allRequestParams) {
    return this.readMany(requestPageSize, null, allRequestParams);
  }

  @GetMapping("/products/{requestPageSize}/{requestPageIndex}")
  public Collection<ProductPojo> readMany(@PathVariable Integer requestPageSize, @PathVariable Integer requestPageIndex,
      @RequestParam Map<String, String> allRequestParams) {
    LOG.info("read");
    Collection<ProductDto> products = this.readFromService(requestPageSize, requestPageIndex, allRequestParams);
    Collection<ProductPojo> productPojos = this.convertCollection(products);
    return productPojos;
  }

  @PutMapping("/product")
  public ProductPojo update(@RequestBody ProductPojo input) {
    LOG.info("update");
    ProductDto dto = conversion.convert(input, ProductDto.class);
    ProductDto processed = crudService.update(dto);
    ProductPojo result = conversion.convert(processed, ProductPojo.class);
    return result;
  }

  @PutMapping("/product/{id}")
  public ProductPojo update(@RequestBody ProductPojo input, @PathVariable Integer id) {
    LOG.info("update");
    ProductDto dto = conversion.convert(input, ProductDto.class);
    ProductDto processed = crudService.update(dto, id);
    ProductPojo result = conversion.convert(processed, ProductPojo.class);
    return result;
  }

  @DeleteMapping("/product/{id}")
  public boolean delete(@PathVariable Integer id) {
    LOG.info("delete");
    boolean result = crudService.delete(id);
    return result;
  }
}
