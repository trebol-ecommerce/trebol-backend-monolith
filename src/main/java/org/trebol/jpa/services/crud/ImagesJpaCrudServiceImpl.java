package org.trebol.jpa.services.crud;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Image;
import org.trebol.jpa.repositories.IImagesJpaRepository;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.ImagePojo;

import java.util.Optional;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Transactional
@Service
public class ImagesJpaCrudServiceImpl
  extends GenericCrudJpaService<ImagePojo, Image> {

  private final IImagesJpaRepository imagesRepository;

  @Autowired
  public ImagesJpaCrudServiceImpl(IImagesJpaRepository repository,
                                  ITwoWayConverterJpaService<ImagePojo, Image> converter) {
    super(repository,
          converter,
          LoggerFactory.getLogger(ImagesJpaCrudServiceImpl.class));
    this.imagesRepository = repository;
  }

  @Override
  public Optional<Image> getExisting(ImagePojo input) throws BadInputException {
    String name = input.getFilename();
    if (name == null || name.isBlank()) {
      throw new BadInputException("Invalid filename");
    } else {
      return imagesRepository.findByFilename(name);
    }
  }
}
