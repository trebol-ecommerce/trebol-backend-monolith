package cl.blm.trebol.store.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.blm.trebol.store.api.pojo.CompanyDetailsPojo;
import cl.blm.trebol.store.services.exposed.CompanyService;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
@RestController
public class CompanyController {

  private final CompanyService companyService;

  @Autowired
  public CompanyController(CompanyService companyService) {
    this.companyService = companyService;
  }

  @GetMapping("/company")
  public CompanyDetailsPojo readCompanyDetails() {
    CompanyDetailsPojo companyDetailsPojo = companyService.readDetails();
    return companyDetailsPojo;
  }
}
