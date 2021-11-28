package org.trebol.operation.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;

@RunWith(SpringRunner.class)
@WebAppConfiguration
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
