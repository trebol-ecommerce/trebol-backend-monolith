package cl.blm.trebol.converters.topojo;

import org.springframework.core.convert.converter.Converter;

import cl.blm.trebol.api.pojo.ImagePojo;
import cl.blm.trebol.jpa.entities.Image;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
public class Image2Pojo
    implements Converter<Image, ImagePojo> {

  @Override
  public ImagePojo convert(Image source) {
    ImagePojo target = new ImagePojo();
    target.setFilename(source.getFilename());
    target.setUrl(source.getUrl());
    return target;
  }

}
