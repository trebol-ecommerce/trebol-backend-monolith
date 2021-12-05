package org.trebol.jpa.services.crud;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Image;
import org.trebol.jpa.repositories.IImagesJpaRepository;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.ImagePojo;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ImagesJpaCrudServiceTest {

  @Mock IImagesJpaRepository imagesRepositoryMock;
  @Mock ITwoWayConverterJpaService<ImagePojo, Image> imagesConverterMock;

  @Test
  public void sanity_check() {
    ImagesJpaCrudServiceImpl service = instantiate();
    assertNotNull(service);
  }

  @Test
  public void finds_by_filename() throws BadInputException {
    Long imageId = 1L;
    String imageCode = "test-one";
    String imageFilename = "test-one.jpg";
    String imageUrl = "test last name";

    ImagePojo example = new ImagePojo(imageFilename);
    Image persistedEntity = new Image(imageId, imageCode, imageFilename, imageUrl);
    when(imagesRepositoryMock.findByFilename(imageFilename)).thenReturn(Optional.of(persistedEntity));

    ImagesJpaCrudServiceImpl service = instantiate();
    Optional<Image> match = service.getExisting(example);

    assertTrue(match.isPresent());
    assertEquals(match.get().getId(), imageId);
    assertEquals(match.get().getCode(), imageCode);
    assertEquals(match.get().getFilename(), imageFilename);
    assertEquals(match.get().getUrl(), imageUrl);
  }

  private ImagesJpaCrudServiceImpl instantiate() {
    return new ImagesJpaCrudServiceImpl(
        imagesRepositoryMock,
        imagesConverterMock
    );
  }

}
