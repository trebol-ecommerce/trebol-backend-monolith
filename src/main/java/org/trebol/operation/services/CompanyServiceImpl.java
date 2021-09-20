package org.trebol.operation.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.trebol.pojo.CompanyDetailsPojo;
import org.trebol.jpa.entities.Param;
import org.trebol.jpa.repositories.IParamsJpaRepository;
import org.trebol.operation.ICompanyService;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
@Service
public class CompanyServiceImpl
    implements ICompanyService {

  private final IParamsJpaRepository paramsRepository;

  @Autowired
  public CompanyServiceImpl(IParamsJpaRepository paramsRepository) {
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
