package org.trebol.jpa.services.conversion;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;
import org.trebol.jpa.entities.Image;
import org.trebol.pojo.ImagePojo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.trebol.constant.TestConstants.ANY;

@ExtendWith(MockitoExtension.class)
class ImagesConverterJpaServiceImplTest {
    @InjectMocks ImagesConverterJpaServiceImpl sut;

    @Mock
    private ConversionService conversionService;

    private Image image;
    private ImagePojo imagePojo;

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
        when(conversionService.convert(any(Image.class), eq(ImagePojo.class))).thenReturn(imagePojo);
        ImagePojo actual = sut.convertToPojo(image);
        assertEquals(ANY, actual.getFilename());
        verify(conversionService, times(1)).convert(any(Image.class), eq(ImagePojo.class));
    }

    @Test
    void testConvertToNewEntity() {
        when(conversionService.convert(any(ImagePojo.class), eq(Image.class))).thenReturn(image);
        Image actual = sut.convertToNewEntity(imagePojo);
        assertEquals(ANY, actual.getFilename());
        verify(conversionService, times(1)).convert(any(ImagePojo.class), eq(Image.class));
    }
}
