package org.trebol.jpa.services;

import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import org.trebol.jpa.entities.QImage;

import org.trebol.pojo.ImagePojo;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Image;
import org.trebol.jpa.GenericJpaService;
import org.trebol.jpa.repositories.IImagesJpaRepository;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Service
public class ImagesJpaServiceImpl
  extends GenericJpaService<ImagePojo, Image> {

  private static final Logger logger = LoggerFactory.getLogger(ImagesJpaServiceImpl.class);
  private final IImagesJpaRepository imagesRepository;
  private final ConversionService conversion;

  @Autowired
  public ImagesJpaServiceImpl(IImagesJpaRepository repository, ConversionService conversion) {
    super(repository);
    this.imagesRepository = repository;
    this.conversion = conversion;
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

  @Override
  public ImagePojo convertToPojo(Image source) {
    return conversion.convert(source, ImagePojo.class);
  }

  @Override
  public Image convertToNewEntity(ImagePojo source) {
    return conversion.convert(source, Image.class);
  }

  @Override
  public void applyChangesToExistingEntity(ImagePojo source, Image target) throws BadInputException {
    String code = source.getCode();
    if (code != null && !code.isBlank() && !target.getCode().equals(code)) {
      target.setCode(code);
    }

    String filename = source.getFilename();
    if (filename != null && !filename.isBlank() && !target.getFilename().equals(filename)) {
      target.setFilename(filename);
    }

    String url = source.getUrl();
    if (url != null && !url.isBlank() && !target.getUrl().equals(url)) {
      target.setUrl(url);
    }
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
