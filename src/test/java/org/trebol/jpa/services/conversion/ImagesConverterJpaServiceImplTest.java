package org.trebol.jpa.services.conversion;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.jpa.entities.Image;
import org.trebol.pojo.ImagePojo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.trebol.constant.TestConstants.ANY;

@ExtendWith(MockitoExtension.class)
class ImagesConverterJpaServiceImplTest {
  @InjectMocks ImagesConverterJpaServiceImpl sut;
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
  void testConvertToPojo() {
    ImagePojo actual = sut.convertToPojo(image);
    assertEquals(image.getFilename(), actual.getFilename());
    assertEquals(image.getCode(), actual.getCode());
  }

  @Test
  void testConvertToNewEntity() {
    Image actual = sut.convertToNewEntity(imagePojo);
    assertEquals(imagePojo.getFilename(), actual.getFilename());
    assertEquals(imagePojo.getCode(), actual.getCode());
  }
}
