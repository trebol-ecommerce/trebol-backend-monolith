package org.trebol.operation.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;

class RootControllerTest {
  @Test
  void return_200OK() {
    given().
      standaloneSetup(new RootController()).
      when().
      get("/").
      then().
      assertThat().
      status(HttpStatus.OK);
  }
}
