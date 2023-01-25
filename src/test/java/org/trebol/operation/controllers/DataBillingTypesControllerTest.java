package org.trebol.operation.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.jpa.entities.BillingType;
import org.trebol.jpa.services.PredicateService;
import org.trebol.jpa.services.SortSpecService;
import org.trebol.jpa.services.crud.BillingTypesCrudService;
import org.trebol.operation.services.PaginationService;
import org.trebol.pojo.BillingTypePojo;
import org.trebol.pojo.DataPagePojo;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataBillingTypesControllerTest {
  @InjectMocks DataBillingTypesController instance;
  @Mock PaginationService paginationServiceMock;
  @Mock SortSpecService<BillingType> sortServiceMock;
  @Mock BillingTypesCrudService crudServiceMock;
  @Mock PredicateService<BillingType> predicateServiceMock;

  @Test
  void reads_billing_types() {
    DataPagePojo<BillingTypePojo> pagePojo = new DataPagePojo<>(0, 0);
    when(crudServiceMock.readMany(anyInt(), anyInt(), eq(null), eq(null))).thenReturn(pagePojo);
    DataPagePojo<BillingTypePojo> result = instance.readMany(Map.of());
    assertNotNull(result);
    assertEquals(0, result.getTotalCount());
    assertTrue(result.getItems().isEmpty());
  }
}
