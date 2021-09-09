package org.trebol.jpa.services;

import java.util.Map;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import org.trebol.jpa.entities.QImage;

import org.trebol.api.pojo.ImagePojo;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Image;
import org.trebol.jpa.GenericJpaCrudService;
import org.trebol.jpa.repositories.IImagesJpaRepository;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Service
public class ImagesJpaCrudServiceImpl
  extends GenericJpaCrudService<ImagePojo, Image> {

  private static final Logger logger = LoggerFactory.getLogger(ImagesJpaCrudServiceImpl.class);
  private final IImagesJpaRepository imagesRepository;
  private final ConversionService conversion;

  @Autowired
  public ImagesJpaCrudServiceImpl(IImagesJpaRepository repository, ConversionService conversion) {
    super(repository);
    this.imagesRepository = repository;
    this.conversion = conversion;
  }

  @Override
  public boolean itemExists(ImagePojo input) throws BadInputException {
    String name = input.getFilename();
    if (name == null || name.isBlank()) {
      throw new BadInputException("Invalid filename");
    } else {
      return (imagesRepository.findByFilename(name).isPresent());
    }
  }

  @Nullable
  @Override
  public ImagePojo entity2Pojo(Image source) {
    return conversion.convert(source, ImagePojo.class);
  }

  @Nullable
  @Override
  public Image pojo2Entity(ImagePojo source) {
    return conversion.convert(source, Image.class);
  }

  @Override
  public Predicate parsePredicate(Map<String, String> queryParamsMap) {
    QImage qImage = QImage.image;
    BooleanBuilder predicate = new BooleanBuilder();
    for (String paramName : queryParamsMap.keySet()) {
      String stringValue = queryParamsMap.get(paramName);
      try {
        switch (paramName) {
          case "id":
            return predicate.and(qImage.id.eq(Long.valueOf(stringValue))); // id matching is final
          case "code":
            return predicate.and(qImage.code.eq(stringValue)); // code matching is final
          case "filename":
            predicate.and(qImage.filename.eq(stringValue));
            break;
          case "filenameLike":
            predicate.and(qImage.filename.likeIgnoreCase("%" + stringValue + "%"));
            break;
          default:
            break;
        }
      } catch (NumberFormatException exc) {
        logger.info("Param '{}' couldn't be parsed as number (value: '{}')", paramName, stringValue);
      }
    }

    return predicate;
  }
}
