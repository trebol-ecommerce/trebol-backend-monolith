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
import cl.blm.newmarketing.backend.pojos.ProductPojo;
import cl.blm.newmarketing.backend.rest.dtos.ProductDto;
import cl.blm.newmarketing.backend.rest.services.CrudService;

/**
 * API point of entry for Client entities
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/api")
public class ProductsController {
  private final static Logger LOG = LoggerFactory.getLogger(ProductsController.class);

  @Autowired
  private ConversionService conversion;
  @Autowired
  private CrudService<ProductDto, Integer> productService;
  @Autowired
  private BackendAppGlobals globals;

  @GetMapping("/products")
  public Collection<ProductPojo> read(@RequestParam Map<String, String> allRequestParams) {
    return this.read(null, null, allRequestParams);
  }

  @GetMapping("/products/{requestPageSize}")
  public Collection<ProductPojo> read(@PathVariable Integer requestPageSize,
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
  @GetMapping("/products/{requestPageSize}/{requestPageIndex}")
  public Collection<ProductPojo> read(@PathVariable Integer requestPageSize, @PathVariable Integer requestPageIndex,
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
      filters = productService.queryParamsMapToPredicate(allRequestParams);
    }

    Collection<ProductDto> products = productService.read(pageSize, pageIndex, filters);
    Collection<ProductPojo> productPojos = (List<ProductPojo>) conversion.convert(products,
        TypeDescriptor.collection(Collection.class, TypeDescriptor.valueOf(ProductDto.class)),
        TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(ProductPojo.class)));
    return productPojos;
  }
}
