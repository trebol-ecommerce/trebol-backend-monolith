package org.trebol.jpa.services.conversion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Product;
import org.trebol.jpa.entities.ProductCategory;
import org.trebol.jpa.entities.ProductImage;
import org.trebol.jpa.repositories.IProductImagesJpaRepository;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.ImagePojo;
import org.trebol.pojo.ProductCategoryPojo;
import org.trebol.pojo.ProductPojo;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Transactional
@Service
public class ProductsConverterJpaServiceImpl
  implements ITwoWayConverterJpaService<ProductPojo, Product> {

  private final IProductImagesJpaRepository productImagesRepository;
  private final ConversionService conversion;

  @Autowired
  public ProductsConverterJpaServiceImpl(IProductImagesJpaRepository productImagesRepository,
                                         ConversionService conversion) {
    this.productImagesRepository = productImagesRepository;
    this.conversion = conversion;
  }

  @Override
  @Nullable
  public ProductPojo convertToPojo(Product source) {
    ProductPojo target = conversion.convert(source, ProductPojo.class);
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

      ProductCategory category = source.getProductCategory();
      if (category != null) {
        ProductCategoryPojo categoryPojo = conversion.convert(category, ProductCategoryPojo.class);
        target.setCategory(categoryPojo);
      }
    }
    return target;
  }

  @Override
  public Product convertToNewEntity(ProductPojo source) throws BadInputException {
    return conversion.convert(source, Product.class);
  }

  @Override
  public Product applyChangesToExistingEntity(ProductPojo source, Product existing) throws BadInputException {
    Product target = new Product(existing);

    String barcode = source.getBarcode();
    if (barcode != null && !barcode.isBlank() && !target.getBarcode().equals(barcode)) {
      target.setBarcode(barcode);
    }

    String name = source.getName();
    if (name != null && !name.isBlank() && !target.getName().equals(name)) {
      target.setName(name);
    }

    Integer price = source.getPrice();
    if (price != null) {
      target.setPrice(price);
    }

    String description = source.getDescription();
    if (description != null) {
      target.setDescription(description);
    }

    Integer currentStock = source.getCurrentStock();
    if (currentStock != null) {
      target.setStockCurrent(currentStock);
    }

    return target;
  }
}
