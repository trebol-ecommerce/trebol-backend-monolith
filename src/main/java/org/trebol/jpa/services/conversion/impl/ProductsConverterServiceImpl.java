/*
 * Copyright (c) 2023 The Trebol eCommerce Project
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

package org.trebol.jpa.services.conversion.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.api.models.ImagePojo;
import org.trebol.api.models.ProductCategoryPojo;
import org.trebol.api.models.ProductPojo;
import org.trebol.jpa.entities.Product;
import org.trebol.jpa.entities.ProductCategory;
import org.trebol.jpa.entities.ProductImage;
import org.trebol.jpa.repositories.ProductImagesRepository;
import org.trebol.jpa.services.conversion.ImagesConverterService;
import org.trebol.jpa.services.conversion.ProductCategoriesConverterService;
import org.trebol.jpa.services.conversion.ProductsConverterService;

import java.util.HashSet;
import java.util.Set;

@Transactional
@Service
public class ProductsConverterServiceImpl
  implements ProductsConverterService {
  private final ProductImagesRepository productImagesRepository;
  private final ImagesConverterService imagesConverterService;
  private final ProductCategoriesConverterService productCategoriesConverterService;

  @Autowired
  public ProductsConverterServiceImpl(
    ProductImagesRepository productImagesRepository,
    ImagesConverterService imagesConverterService,
    ProductCategoriesConverterService productCategoriesConverterService
  ) {
    this.productImagesRepository = productImagesRepository;
    this.imagesConverterService = imagesConverterService;
    this.productCategoriesConverterService = productCategoriesConverterService;
  }

  // TODO this method can be expensive, optimize it to fetch required data only according to context
  @Override
  public ProductPojo convertToPojo(Product source) {
    ProductPojo target = ProductPojo.builder()
      .id(source.getId())
      .name(source.getName())
      .barcode(source.getBarcode())
      .price(source.getPrice())
      .description(source.getDescription())
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
    return Product.builder()
      .name(source.getName())
      .barcode(source.getBarcode())
      .price(source.getPrice())
      .description(source.getDescription())
      .stockCurrent(source.getCurrentStock())
      .stockCritical(source.getCriticalStock())
      .build();
  }

  @Override
  public Product applyChangesToExistingEntity(ProductPojo source, Product target) {
    throw new UnsupportedOperationException("This method is deprecated");
  }
}
