/*
 * Copyright (c) 2023 The Trebol eCommerce Project
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished
 * to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.trebol.api.services.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.api.models.CompanyDetailsPojo;
import org.trebol.jpa.entities.Param;
import org.trebol.jpa.repositories.ParamsRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyServiceImplTest {
  @InjectMocks CompanyServiceImpl instance;
  @Mock ParamsRepository paramsRepositoryMock;

  @DisplayName("It should read get params which contains name and value by category of company map it " +
    "to CompanyDetailsPojo")
  @Test
  void testReadDetails() {

    Param param = Param.builder()
      .name("name")
      .value("Piolo")
      .build();
    Param param2 = Param.builder()
      .name("description")
      .value("guwapo")
      .build();
    Param param3 = Param.builder()
      .name("bannerImageURL")
      .value("anyBannerImageURL")
      .build();
    Param param4 = Param.builder()
      .name("logoImageURL")
      .value("anyLogoImageURL")
      .build();
    Iterable<Param> params = List.of(param, param2, param3, param4);

    when(paramsRepositoryMock.findParamsByCategory("company")).thenReturn(params);

    CompanyDetailsPojo actual = instance.readDetails();


    verify(paramsRepositoryMock, times(1)).findParamsByCategory("company");

    assertEquals("Piolo", actual.getName());
    assertEquals("guwapo", actual.getDescription());
    assertEquals("anyBannerImageURL", actual.getBannerImageURL());
    assertEquals("anyLogoImageURL", actual.getLogoImageURL());

  }

}
