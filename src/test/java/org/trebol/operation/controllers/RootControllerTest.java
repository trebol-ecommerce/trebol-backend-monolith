package org.trebol.operation.controllers;

import org.junit.Test;
import org.springframework.http.HttpStatus;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;

public class RootControllerTest {

  @Test
  public void return_200OK() {
    given().
        standaloneSetup(new RootController()).
    when().
        get("/").
    then().
        assertThat().
        status(HttpStatus.OK);
  }

}
