package org.trebol.jpa.services.crud;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Image;
import org.trebol.jpa.repositories.IImagesJpaRepository;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.ImagePojo;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.trebol.testhelpers.ImagesTestHelper.*;

@ExtendWith(MockitoExtension.class)
public class ImagesJpaCrudServiceTest {

  @Mock IImagesJpaRepository imagesRepositoryMock;
  @Mock ITwoWayConverterJpaService<ImagePojo, Image> imagesConverterMock;

  @Test
  void sanity_check() {
    ImagesJpaCrudServiceImpl service = instantiate();
    assertNotNull(service);
  }

  @Test
  void finds_by_filename() throws BadInputException {
    resetImages();
    when(imagesRepositoryMock.findByFilename(imagePojoForFetch().getFilename())).thenReturn(Optional.of(imageEntityAfterCreation()));
    ImagesJpaCrudServiceImpl service = instantiate();

    Optional<Image> match = service.getExisting(imagePojoForFetch());

    assertTrue(match.isPresent());
    verify(imagesRepositoryMock).findByFilename(imagePojoForFetch().getFilename());
    assertEquals(match.get().getId(), imageEntityAfterCreation().getId());
    assertEquals(match.get().getCode(), imageEntityAfterCreation().getCode());
    assertEquals(match.get().getFilename(), imageEntityAfterCreation().getFilename());
    assertEquals(match.get().getUrl(), imageEntityAfterCreation().getUrl());
  }

  private ImagesJpaCrudServiceImpl instantiate() {
    return new ImagesJpaCrudServiceImpl(
        imagesRepositoryMock,
        imagesConverterMock
    );
  }

}
