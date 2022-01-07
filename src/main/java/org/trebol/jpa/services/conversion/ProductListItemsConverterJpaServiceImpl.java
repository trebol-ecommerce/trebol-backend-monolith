package org.trebol.jpa.services.conversion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.ProductImage;
import org.trebol.jpa.entities.ProductListItem;
import org.trebol.jpa.repositories.IProductImagesJpaRepository;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.ImagePojo;
import org.trebol.pojo.ProductPojo;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

@Transactional
@Service
public class ProductListItemsConverterJpaServiceImpl
  implements ITwoWayConverterJpaService<ProductPojo, ProductListItem> {

  private final IProductImagesJpaRepository productImagesRepository;
  private final ConversionService conversion;

  @Autowired
  public ProductListItemsConverterJpaServiceImpl(IProductImagesJpaRepository productImagesRepository,
                                                 ConversionService conversion) {
    this.productImagesRepository = productImagesRepository;
    this.conversion = conversion;
  }

  @Override
  @Nullable
  public ProductPojo convertToPojo(ProductListItem source) {
    ProductPojo target = conversion.convert(source.getProduct(), ProductPojo.class);
    if (target != null) {
      Long id = target.getId();
      Set<ImagePojo> images = new HashSet<>();
      for (ProductImage pi : productImagesRepository.deepFindProductImagesByProductId(id)) {
        ImagePojo targetImage = conversion.convert(pi.getImage(), ImagePojo.class);
        if (targetImage != null) {
          images.add(targetImage);
        }
      }
      target.setImages(images);
    }
    return target;
  }

  @Override
  public ProductListItem convertToNewEntity(ProductPojo source) throws BadInputException {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public ProductListItem applyChangesToExistingEntity(ProductPojo source, ProductListItem existing) throws BadInputException {
    throw new UnsupportedOperationException("Not implemented");
  }
}
