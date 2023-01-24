package org.trebol.operation.controllers;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.jpa.entities.Image;
import org.trebol.jpa.services.CrudGenericService;
import org.trebol.jpa.services.PredicateService;
import org.trebol.jpa.services.SortSpecService;
import org.trebol.operation.PaginationService;
import org.trebol.pojo.ImagePojo;

@ExtendWith(MockitoExtension.class)
class DataImagesControllerTest {
  @InjectMocks DataImagesController instance;
  @Mock PaginationService paginationService;
  @Mock SortSpecService<Image> sortService;
  @Mock CrudGenericService<ImagePojo, Image> crudService;
  @Mock PredicateService<Image> predicateService;

  // TODO write a test
}
