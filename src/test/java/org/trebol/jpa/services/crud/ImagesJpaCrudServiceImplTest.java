package org.trebol.jpa.services.crud;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Image;
import org.trebol.jpa.repositories.IImagesJpaRepository;
import org.trebol.pojo.ImagePojo;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.trebol.testhelpers.ImagesTestHelper.*;

@ExtendWith(MockitoExtension.class)
class ImagesJpaCrudServiceImplTest {
  @InjectMocks ImagesJpaCrudServiceImpl instance;
  @Mock IImagesJpaRepository imagesRepositoryMock;

  @BeforeEach
  void beforeEach() {
    resetImages();
  }

  @Test
  void finds_by_filename() throws BadInputException {
    ImagePojo input = imagePojoForFetch();
    Image expectedResult = imageEntityAfterCreation();
    when(imagesRepositoryMock.findByFilename(anyString())).thenReturn(Optional.of(expectedResult));

    Optional<Image> match = instance.getExisting(input);

    verify(imagesRepositoryMock).findByFilename(input.getFilename());
    assertTrue(match.isPresent());
    assertEquals(expectedResult, match.get());
  }

}
