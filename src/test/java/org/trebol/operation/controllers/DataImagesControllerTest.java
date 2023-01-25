package org.trebol.operation.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.jpa.entities.Image;
import org.trebol.jpa.services.PredicateService;
import org.trebol.jpa.services.SortSpecService;
import org.trebol.jpa.services.crud.ImagesCrudService;
import org.trebol.operation.services.PaginationService;
import org.trebol.pojo.DataPagePojo;
import org.trebol.pojo.ImagePojo;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataImagesControllerTest {
  @InjectMocks DataImagesController instance;
  @Mock PaginationService paginationService;
  @Mock SortSpecService<Image> sortService;
  @Mock ImagesCrudService crudServiceMock;
  @Mock PredicateService<Image> predicateService;

  @Test
  void reads_images() {
    DataPagePojo<ImagePojo> pagePojo = new DataPagePojo<>(0, 0);
    when(crudServiceMock.readMany(anyInt(), anyInt(), eq(null), eq(null))).thenReturn(pagePojo);
    DataPagePojo<ImagePojo> result = instance.readMany(Map.of());
    assertNotNull(result);
    assertEquals(0, result.getTotalCount());
    assertTrue(result.getItems().isEmpty());
  }
}
