package cl.blm.trebol.store.services.exposed.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.blm.trebol.store.api.pojo.CompanyDetailsPojo;
import cl.blm.trebol.store.jpa.entities.Param;
import cl.blm.trebol.store.jpa.repositories.ParamsRepository;
import cl.blm.trebol.store.services.exposed.CompanyService;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
@Service
public class CompanyServiceImpl
    implements CompanyService {

  private final ParamsRepository paramsRepository;

  @Autowired
  public CompanyServiceImpl(ParamsRepository paramsRepository) {
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
