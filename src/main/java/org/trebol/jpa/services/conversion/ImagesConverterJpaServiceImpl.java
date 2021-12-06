package org.trebol.jpa.services.conversion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Image;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.ImagePojo;

import javax.annotation.Nullable;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Service
public class ImagesConverterJpaServiceImpl
  implements ITwoWayConverterJpaService<ImagePojo, Image> {

  private final ConversionService conversion;

  @Autowired
  public ImagesConverterJpaServiceImpl(ConversionService conversion) {
    this.conversion = conversion;
  }

  @Override
  @Nullable
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
}
