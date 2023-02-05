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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trebol.api.models.CompanyDetailsPojo;
import org.trebol.api.services.CompanyService;
import org.trebol.jpa.entities.Param;
import org.trebol.jpa.repositories.ParamsRepository;

@Service
public class CompanyServiceImpl
  implements CompanyService {
  private final ParamsRepository paramsRepository;

  @Autowired
  public CompanyServiceImpl(
    ParamsRepository paramsRepository
  ) {
    this.paramsRepository = paramsRepository;
  }

  @Override
  public CompanyDetailsPojo readDetails() {
    Iterable<Param> it = paramsRepository.findParamsByCategory("company");
    CompanyDetailsPojo target = new CompanyDetailsPojo();
    for (Param p : it) {
      String v = p.getValue();
      switch (p.getName()) {
        case "name":
          target.setName(v);
          break;
        case "description":
          target.setDescription(v);
          break;
        case "bannerImageURL":
          target.setBannerImageURL(v);
          break;
        case "logoImageURL":
          target.setLogoImageURL(v);
          break;
        default:
          break;
      }
    }
    return target;
  }
}
