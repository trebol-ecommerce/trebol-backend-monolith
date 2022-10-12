package org.trebol;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.trebol.operation.controllers.RootController;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class BackendAppTest {

  @Autowired
  RootController rootController;

  @Test
  void contextLoads() {
    assertThat(rootController).isNotNull();
  }
}
