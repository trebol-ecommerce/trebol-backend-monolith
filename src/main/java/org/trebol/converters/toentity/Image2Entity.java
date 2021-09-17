package org.trebol.converters.toentity;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import org.trebol.pojo.ImagePojo;
import org.trebol.jpa.entities.Image;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class Image2Entity
    implements Converter<ImagePojo, Image> {

  @Override
  public Image convert(ImagePojo source) {
    Image target = new Image();
    target.setCode(source.getCode());
    target.setFilename(source.getFilename());
    target.setUrl(source.getUrl());
    return target;
  }
}
