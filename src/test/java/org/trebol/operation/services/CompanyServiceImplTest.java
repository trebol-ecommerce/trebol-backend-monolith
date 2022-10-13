package org.trebol.operation.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.jpa.entities.Param;
import org.trebol.jpa.repositories.IParamsJpaRepository;
import org.trebol.pojo.CompanyDetailsPojo;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompanyServiceImplTest {

  @InjectMocks
  private CompanyServiceImpl sut;

  @Mock
  private IParamsJpaRepository paramsRepository;


  @DisplayName("It should read get params which contains name and value by category of company map it " +
    "to CompanyDetailsPojo")
  @Test
  void testReadDetails() {

    Param param = new Param();
    param.setName("name");
    param.setValue("Piolo");
    Param param2 = new Param();
    param2.setName("description");
    param2.setValue("guwapo");
    Param param3 = new Param();
    param3.setName("bannerImageURL");
    param3.setValue("anyBannerImageURL");
    Param param4 = new Param();
    param4.setName("logoImageURL");
    param4.setValue("anyLogoImageURL");
    Iterable<Param> params = List.of(param, param2, param3, param4);

    when(paramsRepository.findParamsByCategory("company")).thenReturn(params);

    CompanyDetailsPojo actual = sut.readDetails();


    verify(paramsRepository, times(1)).findParamsByCategory("company");

    assertEquals("Piolo", actual.getName());
    assertEquals("guwapo", actual.getDescription());
    assertEquals("anyBannerImageURL", actual.getBannerImageURL());
    assertEquals("anyLogoImageURL", actual.getLogoImageURL());

  }

}
