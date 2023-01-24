/*
 * Copyright (c) 2022 The Trebol eCommerce Project
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished
 * to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.trebol.jpa.services.conversion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Product;
import org.trebol.jpa.entities.ProductCategory;
import org.trebol.jpa.entities.ProductImage;
import org.trebol.jpa.repositories.ProductImagesJpaRepository;
import org.trebol.pojo.ImagePojo;
import org.trebol.pojo.ProductCategoryPojo;
import org.trebol.pojo.ProductPojo;

import java.util.HashSet;
import java.util.Set;

@Transactional
@Service
public class ProductsConverterServiceImpl
  implements ProductsConverterService {

  private final ProductImagesJpaRepository productImagesRepository;
  private final ImagesConverterService imagesConverterService;
  private final ProductCategoriesConverterService productCategoriesConverterService;

  @Autowired
  public ProductsConverterServiceImpl(ProductImagesJpaRepository productImagesRepository,
                                      ImagesConverterService imagesConverterService,
                                      ProductCategoriesConverterService productCategoriesConverterService) {
    this.productImagesRepository = productImagesRepository;
    this.imagesConverterService = imagesConverterService;
    this.productCategoriesConverterService = productCategoriesConverterService;
  }

  // TODO this method can be expensive
  @Override
  public ProductPojo convertToPojo(Product source) {
    ProductPojo target = ProductPojo.builder()
      .id(source.getId())
      .name(source.getName())
      .barcode(source.getBarcode())
      .price(source.getPrice())
      .currentStock(source.getStockCurrent())
      .criticalStock(source.getStockCritical())
      .build();
    Set<ImagePojo> images = new HashSet<>();
    for (ProductImage pi : productImagesRepository.deepFindProductImagesByProductId(source.getId())) {
      ImagePojo targetImage = imagesConverterService.convertToPojo(pi.getImage());
      if (targetImage != null) {
        images.add(targetImage);
      }
    }
    target.setImages(images);

    ProductCategory category = source.getProductCategory();
    if (category != null) {
      ProductCategoryPojo categoryPojo = productCategoriesConverterService.convertToPojo(category);
      target.setCategory(categoryPojo);
    }
    return target;
  }

  @Override
  public Product convertToNewEntity(ProductPojo source) {
    Product target = new Product();
    target.setName(source.getName());
    target.setBarcode(source.getBarcode());
    target.setPrice(source.getPrice());
    if (source.getCurrentStock() != null) {
      target.setStockCurrent(source.getCurrentStock());
    }
    if (source.getCriticalStock() != null) {
      target.setStockCritical(source.getCriticalStock());
    }
    return target;
  }

  @Override
  public Product applyChangesToExistingEntity(ProductPojo source, Product target) throws BadInputException {
    throw new UnsupportedOperationException("This method is deprecated");
  }
}
