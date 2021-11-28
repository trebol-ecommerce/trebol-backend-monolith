package org.trebol.operation.controllers;

import com.querydsl.core.types.Predicate;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.trebol.config.OperationProperties;
import org.trebol.jpa.GenericJpaService;
import org.trebol.jpa.entities.Product;
import org.trebol.pojo.DataPagePojo;
import org.trebol.pojo.ProductPojo;

import java.util.Map;

import static org.mockito.Mockito.*;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@WebAppConfiguration
public class PublicProductsControllerTest {

  @Mock
  GenericJpaService<ProductPojo, Product> productCrudService;

  @Mock
  OperationProperties operationProperties;

  @Test
  public void return_dataPage() {
    int defaultPageSize = 8;
    int pageSize = 10;
    int pageIndex = 0;
    Map<String, String> queryParamsMap = Map.of(
        "pageIndex", String.valueOf(pageIndex),
        "pageSize", String.valueOf(pageSize));
    Predicate filters = null;

    when(operationProperties.getItemsPerPage()).
        thenReturn(defaultPageSize);
    when(productCrudService.parsePredicate(any())).
        thenReturn(filters);
    when(productCrudService.readMany(eq(pageSize), eq(pageIndex), any())).
        thenReturn(new DataPagePojo<>(pageIndex, pageSize));

    given().
        standaloneSetup(new PublicProductsController(productCrudService, operationProperties)).
        queryParam("pageIndex", String.valueOf(pageIndex)).
        queryParam("pageSize", String.valueOf(pageSize)).
    when().
        get("/public/products").
    then().
        status(HttpStatus.OK).
        body("items", equalTo(Lists.emptyList())).
        body("pageIndex", equalTo(pageIndex)).
        body("pageSize", equalTo(pageSize));

    verify(productCrudService).parsePredicate(queryParamsMap);
    verify(productCrudService).readMany(pageSize, pageIndex, filters);
  }

}
