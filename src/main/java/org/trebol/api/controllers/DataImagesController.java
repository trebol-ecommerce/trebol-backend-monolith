package org.trebol.api.controllers;

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
import org.trebol.api.DataPage;
import org.trebol.api.GenericDataController;
import org.trebol.config.CustomProperties;
import org.trebol.jpa.entities.Image;
import org.trebol.exceptions.EntityAlreadyExistsException;
import org.trebol.jpa.GenericJpaCrudService;

import com.querydsl.core.types.Predicate;

import org.trebol.api.IDataCrudController;
import org.trebol.exceptions.BadInputException;

import javassist.NotFoundException;

/**
 * API point of entry for Image entities
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/data/images")
public class DataImagesController
  extends GenericDataController<ImagePojo, Image>
  implements IDataCrudController<ImagePojo, String> {

  @Autowired
  public DataImagesController(CustomProperties globals, GenericJpaCrudService<ImagePojo, Image> crudService) {
    super(globals, crudService);
  }

  @GetMapping({"", "/"})
  @PreAuthorize("hasAuthority('images:read')")
  public DataPage<ImagePojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    return super.readMany(null, null, allRequestParams);
  }

  @Override
  @PostMapping({"", "/"})
  @PreAuthorize("hasAuthority('images:create')")
  public void create(@Valid @RequestBody ImagePojo input) throws BadInputException, EntityAlreadyExistsException {
    crudService.create(input);
  }

  @Override
  @GetMapping({"/{code}", "/{code}/"})
  @PreAuthorize("hasAuthority('images:read')")
  public ImagePojo readOne(@PathVariable String code) throws NotFoundException {
    Map<String, String> codeMatchMap = Maps.of("code", code).build();
    Predicate filters = crudService.parsePredicate(codeMatchMap);
    return crudService.readOne(filters);
  }

  @Override
  @PutMapping({"/{code}", "/{code}/"})
  @PreAuthorize("hasAuthority('images:update')")
  public void update(@RequestBody ImagePojo input, @PathVariable String code)
    throws NotFoundException, BadInputException {
    // TODO improve this implementation; the same customer will be fetched twice
    Long imageId = this.readOne(code).getId();
    crudService.update(input, imageId);
  }

  @Override
  @DeleteMapping({"/{code}", "/{code}/"})
  @PreAuthorize("hasAuthority('images:delete')")
  public void delete(@PathVariable String code) throws NotFoundException {
    Long imageId = this.readOne(code).getId();
    crudService.delete(imageId);
  }
}
