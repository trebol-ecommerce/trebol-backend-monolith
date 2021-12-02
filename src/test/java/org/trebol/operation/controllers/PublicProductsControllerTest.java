package org.trebol.operation.controllers;

import com.querydsl.core.types.Predicate;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.trebol.config.OperationProperties;
import org.trebol.jpa.entities.Product;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.IPredicateJpaService;
import org.trebol.pojo.DataPagePojo;
import org.trebol.pojo.ProductPojo;

import java.util.Map;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PublicProductsControllerTest {

  @Mock
  GenericCrudJpaService<ProductPojo, Product> productCrudService;

  @Mock
  IPredicateJpaService<Product> predicateService;

  @Mock
  OperationProperties operationProperties;

  @Test
  public void return_dataPage() {
    int pageSize = 10;
    int pageIndex = 0;
    Map<String, String> queryParamsMap = Map.of(
        "pageIndex", String.valueOf(pageIndex),
        "pageSize", String.valueOf(pageSize));
    Predicate filters = null;

    when(predicateService.parseMap(any())).
        thenReturn(filters);
    when(productCrudService.readMany(eq(pageSize), eq(pageIndex), any())).
        thenReturn(new DataPagePojo<>(pageIndex, pageSize));

    given().
        standaloneSetup(new PublicProductsController(productCrudService, predicateService, operationProperties)).
        queryParam("pageIndex", String.valueOf(pageIndex)).
        queryParam("pageSize", String.valueOf(pageSize)).
    when().
        get("/public/products").
    then().
        assertThat().
        status(HttpStatus.OK).
        body("items", equalTo(Lists.emptyList())).
        body("pageIndex", equalTo(pageIndex)).
        body("pageSize", equalTo(pageSize));

    verify(predicateService).parseMap(queryParamsMap);
    verify(productCrudService).readMany(pageSize, pageIndex, filters);
  }

}
