package org.trebol.jpa.services.crud;


import org.junit.Test;
import org.mockito.Mock;
import org.trebol.jpa.entities.Image;
import org.trebol.jpa.repositories.IImagesJpaRepository;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.ImagePojo;

import static org.junit.jupiter.api.Assertions.assertNotNull;


public class ImagesJpaCrudServiceTest {

  @Mock IImagesJpaRepository imagesRepositoryMock;
  @Mock ITwoWayConverterJpaService<ImagePojo, Image> imagesConverterMock;

  @Test
  public void sanity_check() {
    ImagesJpaCrudServiceImpl service = new ImagesJpaCrudServiceImpl(
        imagesRepositoryMock,
        imagesConverterMock
    );
    assertNotNull(service);
  }

}
