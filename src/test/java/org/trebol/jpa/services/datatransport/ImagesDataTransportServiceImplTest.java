package org.trebol.jpa.services.datatransport;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Image;
import org.trebol.pojo.ImagePojo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.trebol.constant.TestConstants.ANY;

@ExtendWith(MockitoExtension.class)
class ImagesDataTransportServiceImplTest {
  @InjectMocks ImagesDataTransportServiceImpl sut;
  Image image;
  ImagePojo imagePojo;

  @BeforeEach
  void beforeEach() {
    image = new Image();
    image.setId(1L);
    image.setFilename(ANY);
    imagePojo = ImagePojo.builder()
      .id(1L)
      .filename(ANY)
      .build();
  }

  @AfterEach
  void afterEach() {
    image = null;
    imagePojo = null;
  }

  @Test
  void testApplyChangesToExistingEntity() throws BadInputException {
    Image actual = sut.applyChangesToExistingEntity(imagePojo, image);
    assertEquals(1L, actual.getId());

    image.setFilename(ANY);
    image.setCode(ANY);
    image.setUrl(ANY);

    imagePojo.setUrl(ANY + " ");
    imagePojo.setCode(ANY + " ");
    imagePojo.setFilename(ANY + " ");

    actual = sut.applyChangesToExistingEntity(imagePojo, image);

    assertEquals(ANY + " ", actual.getUrl());
    assertEquals(ANY + " ", actual.getCode());
    assertEquals(ANY + " ", actual.getFilename());
  }
}
