package org.trebol.operation.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.jpa.entities.Image;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.IPredicateJpaService;
import org.trebol.jpa.services.ISortSpecJpaService;
import org.trebol.operation.PaginationService;
import org.trebol.pojo.ImagePojo;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class DataImagesControllerTest {

  @Mock PaginationService paginationService;
  @Mock ISortSpecJpaService<Image> sortService;
  @Mock GenericCrudJpaService<ImagePojo, Image> crudService;
  @Mock IPredicateJpaService<Image> predicateService;

  @Test
  void sanity_check() {
    DataImagesController service = instantiate();
    assertNotNull(service);
  }

  private DataImagesController instantiate() {
    return new DataImagesController(
            paginationService,
            sortService,
            crudService,
            predicateService
    );
  }

}
