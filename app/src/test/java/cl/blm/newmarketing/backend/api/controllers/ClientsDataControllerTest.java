package cl.blm.newmarketing.backend.api.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cl.blm.newmarketing.backend.BackendTestApp;

@RunWith(value = SpringRunner.class)
@SpringBootTest(classes = { BackendTestApp.class })
public class ClientsDataControllerTest {

  @Autowired
  private ClientsDataController clientsDataController;

  @Test
  public void contextLoads() {
    assertThat(clientsDataController).isNotNull();
  }
}
