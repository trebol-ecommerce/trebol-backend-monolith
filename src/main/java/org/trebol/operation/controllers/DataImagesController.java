package org.trebol.operation.controllers;

import java.util.Map;

import javax.validation.Valid;

import io.jsonwebtoken.lang.Maps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.trebol.pojo.ImagePojo;
import org.trebol.pojo.DataPagePojo;
import org.trebol.operation.GenericDataController;
import org.trebol.config.OperationProperties;
import org.trebol.jpa.entities.Image;
import org.trebol.exceptions.EntityAlreadyExistsException;
import org.trebol.jpa.GenericJpaService;

import com.querydsl.core.types.Predicate;

import org.trebol.operation.IDataCrudController;
import org.trebol.exceptions.BadInputException;

import javassist.NotFoundException;

/**
 * Controller that maps API resources for CRUD operations on Images
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/data/images")
@PreAuthorize("isAuthenticated()")
public class DataImagesController
  extends GenericDataController<ImagePojo, Image>
  implements IDataCrudController<ImagePojo, String> {

  @Autowired
  public DataImagesController(OperationProperties globals, GenericJpaService<ImagePojo, Image> crudService) {
    super(globals, crudService);
  }

  @GetMapping({"", "/"})
  @PreAuthorize("hasAuthority('images:read')")
  public DataPagePojo<ImagePojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    return super.readMany(null, null, allRequestParams);
  }

  @Override
  @PostMapping({"", "/"})
  @PreAuthorize("hasAuthority('images:create')")
  public void create(@Valid @RequestBody ImagePojo input) throws BadInputException, EntityAlreadyExistsException {
    crudService.create(input);
  }

  @Override
  @PutMapping({"", "/"})
  @PreAuthorize("hasAuthority('images:update')")
  public void update(@RequestBody ImagePojo input, @RequestParam Map<String, String> requestParams)
      throws NotFoundException, BadInputException {
    if (!requestParams.isEmpty()) {
      Predicate predicate = crudService.parsePredicate(requestParams);
      ImagePojo existing = crudService.readOne(predicate);
      crudService.update(input, existing.getId());
    } else {
      crudService.update(input);
    }
  }

  @Override
  @DeleteMapping({"", "/"})
  @PreAuthorize("hasAuthority('images:delete')")
  public void delete(@RequestParam Map<String, String> requestParams) throws NotFoundException {
    Predicate predicate = crudService.parsePredicate(requestParams);
    crudService.delete(predicate);
  }

  @Deprecated
  @GetMapping({"/{code}", "/{code}/"})
  @PreAuthorize("hasAuthority('images:read')")
  public ImagePojo readOne(@PathVariable String code) throws NotFoundException {
    Map<String, String> codeMatchMap = Maps.of("code", code).build();
    Predicate filters = crudService.parsePredicate(codeMatchMap);
    return crudService.readOne(filters);
  }

  @Deprecated
  @PutMapping({"/{code}", "/{code}/"})
  @PreAuthorize("hasAuthority('images:update')")
  public void update(@RequestBody ImagePojo input, @PathVariable String code)
    throws NotFoundException, BadInputException {
    // TODO improve this implementation; the same customer will be fetched twice
    Long imageId = this.readOne(code).getId();
    crudService.update(input, imageId);
  }

  @Deprecated
  @DeleteMapping({"/{code}", "/{code}/"})
  @PreAuthorize("hasAuthority('images:delete')")
  public void delete(@PathVariable String code) throws NotFoundException {
    Long imageId = this.readOne(code).getId();
    crudService.delete(imageId);
  }
}
