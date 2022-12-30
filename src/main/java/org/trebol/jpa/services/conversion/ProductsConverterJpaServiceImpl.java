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
import org.springframework.core.convert.ConversionService;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Product;
import org.trebol.jpa.entities.ProductCategory;
import org.trebol.jpa.entities.ProductImage;
import org.trebol.jpa.repositories.IProductImagesJpaRepository;
import org.trebol.pojo.ImagePojo;
import org.trebol.pojo.ProductCategoryPojo;
import org.trebol.pojo.ProductPojo;

import java.util.HashSet;
import java.util.Set;

@Transactional
@Service
public class ProductsConverterJpaServiceImpl
  implements IProductsConverterJpaService {

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
  public Product applyChangesToExistingEntity(ProductPojo source, Product target) throws BadInputException {
    throw new UnsupportedOperationException("This method is deprecated");
  }
}
