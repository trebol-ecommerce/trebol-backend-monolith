package org.trebol.operation.controllers;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.jpa.entities.Image;
import org.trebol.jpa.services.GenericCrudService;
import org.trebol.jpa.services.IPredicateService;
import org.trebol.jpa.services.ISortSpecService;
import org.trebol.operation.PaginationService;
import org.trebol.pojo.ImagePojo;

@ExtendWith(MockitoExtension.class)
class DataImagesControllerTest {
  @InjectMocks DataImagesController instance;
  @Mock PaginationService paginationService;
  @Mock ISortSpecService<Image> sortService;
  @Mock GenericCrudService<ImagePojo, Image> crudService;
  @Mock IPredicateService<Image> predicateService;

  // TODO write a test
}
