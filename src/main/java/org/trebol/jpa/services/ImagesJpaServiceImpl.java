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

  private final IImagesJpaRepository imagesRepository;
  private final ConversionService conversion;

  @Autowired
  public ImagesJpaServiceImpl(IImagesJpaRepository repository, ConversionService conversion) {
    super(repository, LoggerFactory.getLogger(ImagesJpaServiceImpl.class));
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
  public Image applyChangesToExistingEntity(ImagePojo source, Image existing) throws BadInputException {
    Image target = new Image(existing);

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

    return target;
  }

  @Override
  public Predicate parsePredicate(Map<String, String> queryParamsMap) {
    QImage qImage = QImage.image;
    BooleanBuilder predicate = new BooleanBuilder();
    for (Map.Entry<String, String> entry : queryParamsMap.entrySet()) {
      String paramName = entry.getKey();
      String stringValue = entry.getValue();
      try {
        switch (paramName) {
          case "id":
            return qImage.id.eq(Long.valueOf(stringValue));
          case "code":
            return qImage.code.eq(stringValue);
          case "filename":
            return qImage.filename.eq(stringValue);
          case "codeLike":
            predicate.and(qImage.code.likeIgnoreCase("%" + stringValue + "%"));
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
