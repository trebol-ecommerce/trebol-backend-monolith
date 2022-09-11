package org.trebol.jpa.services.conversion;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Image;
import org.trebol.pojo.ImagePojo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.trebol.constant.TestContants.ANY;

@ExtendWith(MockitoExtension.class)
class ImagesConverterJpaServiceImplTest {

    @InjectMocks
    private ImagesConverterJpaServiceImpl sut;

    @Mock
    private ConversionService conversionService;

    private Image image;
    private ImagePojo imagePojo;

    @BeforeEach
    void beforeEach() {
        image = new Image();
        image.setId(1L);
        image.setFilename(ANY);

        imagePojo = new ImagePojo();
        imagePojo.setId(1L);
        imagePojo.setFilename(ANY);
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
