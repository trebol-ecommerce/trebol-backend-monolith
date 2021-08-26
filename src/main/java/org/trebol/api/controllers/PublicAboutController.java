package org.trebol.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.trebol.api.pojo.CompanyDetailsPojo;
import org.trebol.api.services.CompanyService;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
@RestController
@RequestMapping("/public/about")
public class PublicAboutController {

  private final CompanyService companyService;

  @Autowired
  public PublicAboutController(CompanyService companyService) {
    this.companyService = companyService;
  }

  @GetMapping({"", "/"})
  public CompanyDetailsPojo readCompanyDetails() {
    CompanyDetailsPojo companyDetailsPojo = companyService.readDetails();
    return companyDetailsPojo;
  }
}
