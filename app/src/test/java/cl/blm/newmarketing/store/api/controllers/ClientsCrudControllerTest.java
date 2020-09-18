package cl.blm.newmarketing.store.api.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cl.blm.newmarketing.store.BackendTestApp;
import cl.blm.newmarketing.store.api.controllers.crud.ClientsCrudController;

@RunWith(value = SpringRunner.class)
@SpringBootTest(classes = { BackendTestApp.class })
public class ClientsCrudControllerTest {

  @Autowired
  private ClientsCrudController clientsDataController;

  @Test
  public void contextLoads() {
    assertThat(clientsDataController).isNotNull();
  }
}
