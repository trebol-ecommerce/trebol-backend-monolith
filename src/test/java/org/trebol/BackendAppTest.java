package org.trebol;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.trebol.operation.controllers.RootController;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
public class BackendAppTest {

  @Autowired RootController rootController;

  @Test
  public void contextLoads() {
    assertThat(rootController).isNotNull();
  }
}
