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

package org.trebol.jpa.services.crud;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.exceptions.BadInputException;
import org.trebol.exceptions.EntityAlreadyExistsException;
import org.trebol.jpa.entities.Image;
import org.trebol.jpa.entities.Product;
import org.trebol.jpa.entities.ProductCategory;
import org.trebol.jpa.entities.ProductImage;
import org.trebol.jpa.repositories.IImagesJpaRepository;
import org.trebol.jpa.repositories.IProductImagesJpaRepository;
import org.trebol.jpa.repositories.IProductsJpaRepository;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.ImagePojo;
import org.trebol.pojo.ProductCategoryPojo;
import org.trebol.pojo.ProductPojo;

import javax.validation.Validator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

;
@Transactional
@Service
public class ProductsJpaCrudServiceImpl
  extends GenericCrudJpaService<ProductPojo, Product> {

  private final IProductsJpaRepository productsRepository;
  private final IImagesJpaRepository imagesRepository;
  private final IProductImagesJpaRepository productImagesRepository;
  private final GenericCrudJpaService<ImagePojo, Image> imagesCrudService;
  private final GenericCrudJpaService<ProductCategoryPojo, ProductCategory> categoriesCrudService;
  private final ITwoWayConverterJpaService<ProductCategoryPojo, ProductCategory> categoriesConverter;
  private final ITwoWayConverterJpaService<ImagePojo, Image> imageConverter;
  private final Validator validator;

  @Autowired
  public ProductsJpaCrudServiceImpl(IProductsJpaRepository repository,
                                    ITwoWayConverterJpaService<ProductPojo, Product> converter,
                                    IImagesJpaRepository imagesRepository,
                                    IProductImagesJpaRepository productImagesRepository,
                                    GenericCrudJpaService<ImagePojo, Image> imagesCrudService,
                                    GenericCrudJpaService<ProductCategoryPojo, ProductCategory> categoriesService,
                                    ITwoWayConverterJpaService<ProductCategoryPojo, ProductCategory> categoriesConverter,
                                    ITwoWayConverterJpaService<ImagePojo, Image> imageConverter,
                                    Validator validator) {
    super(repository,
          converter,
          LoggerFactory.getLogger(ProductsJpaCrudServiceImpl.class));
    this.productsRepository = repository;
    this.imagesCrudService = imagesCrudService;
    this.categoriesConverter = categoriesConverter;
    this.imagesRepository = imagesRepository;
    this.productImagesRepository = productImagesRepository;
    this.categoriesCrudService = categoriesService;
    this.imageConverter = imageConverter;
    this.validator = validator;
  }

  @Transactional
  @Override
  public ProductPojo create(ProductPojo inputPojo) throws BadInputException, EntityAlreadyExistsException {
    ProductPojo outputPojo = super.create(inputPojo);

    Collection<ImagePojo> pojoImages = inputPojo.getImages();
    if (pojoImages != null && !pojoImages.isEmpty()) {
      Product target = productsRepository.getOne(outputPojo.getId());
      this.saveProductImagesAndReturnAsPojos(target, pojoImages);
    }

    ProductCategoryPojo inputCategory = inputPojo.getCategory();
    if (inputCategory != null && validator.validate(inputCategory).isEmpty()) {
      ProductCategoryPojo outputCategory = this.saveCategory(outputPojo.getId(), inputCategory);
      outputPojo.setCategory(outputCategory);
    }

    return outputPojo;
  }

  @Override
  public Optional<Product> getExisting(ProductPojo input) throws BadInputException {
    String barcode = input.getBarcode();
    if (barcode == null || barcode.isEmpty()) {
      throw new BadInputException("Invalid product barcode");
    } else {
      return productsRepository.findByBarcode(barcode);
    }
  }

  @Override
  protected ProductPojo doUpdate(ProductPojo inputPojo, Product existingEntity) throws BadInputException {
    Product updatedEntity = converter.applyChangesToExistingEntity(inputPojo, existingEntity);
    updatedEntity.setProductCategory(null);
    updatedEntity = productsRepository.saveAndFlush(updatedEntity);
    ProductPojo outputPojo = converter.convertToPojo(updatedEntity);
    assert outputPojo != null; // because entity has just been saved and flushed

    productImagesRepository.deleteByProductId(updatedEntity.getId());
    Collection<ImagePojo> pojoImages = inputPojo.getImages();
    if (pojoImages != null) {
      List<ImagePojo> imagePojos = this.saveProductImagesAndReturnAsPojos(updatedEntity, pojoImages);
      outputPojo.setImages(imagePojos);
    }

    ProductCategoryPojo inputCategory = inputPojo.getCategory();
    if (inputCategory != null && validator.validate(inputCategory).isEmpty()) {
      ProductCategoryPojo categoryPojo = this.saveCategory(updatedEntity.getId(), inputCategory);
      outputPojo.setCategory(categoryPojo);
    }

    return outputPojo;
  }

  /**
   * Saves a product's relationship to a category (and if the category doesn't exist, creates it beforehand)
   * @param entityId The entity ID of the target product
   * @param inputCategory The Pojo for the category to associate
   * @return The resulting category's Pojo equivalent
   * @throws BadInputException If any BadInputException is subsequently thrown
   */
  private ProductCategoryPojo saveCategory(Long entityId, ProductCategoryPojo inputCategory) throws BadInputException {
    ProductCategoryPojo outputCategory;
    try {
      outputCategory = categoriesCrudService.create(inputCategory);
    } catch (EntityAlreadyExistsException ex) {
      Optional<ProductCategory> existing = categoriesCrudService.getExisting(inputCategory);
      if (existing.isPresent()) {
        outputCategory = categoriesConverter.convertToPojo(existing.get());
        assert outputCategory != null; // because an entity was actually found
      } else {
        throw new RuntimeException("Persistence context mismatch - Existing category couldn't be found");
      }
    }
    productsRepository.setProductCategoryById(entityId, outputCategory.getId());
    return outputCategory;
  }

  private List<ImagePojo> saveProductImagesAndReturnAsPojos(Product updatedEntity, Collection<ImagePojo> inputImages)
      throws BadInputException {
    List<ImagePojo> outputImages = new ArrayList<>();
    List<ProductImage> targetImages = new ArrayList<>();
    for (ImagePojo img : inputImages) {
      if (img != null && validator.validate(img).isEmpty()) {
        ImagePojo outputPojo = this.saveImage(img);
        outputImages.add(outputPojo);
        ProductImage targetImage = new ProductImage();
        targetImage.setImage(imagesRepository.getOne(outputPojo.getId()));
        targetImage.setProduct(updatedEntity);
        targetImages.add(targetImage);
      }
    }
    productImagesRepository.saveAll(targetImages);
    return outputImages;
  }

  /**
   * Forcefully saves and returns an image.
   * Here 'forcefully' means that it will always try to create the image, and if it fails, to fetch it.
   * @param inputPojo The Pojo for the image to associate
   * @return The resulting image's Pojo equivalent
   * @throws BadInputException If any BadInputException is subsequently thrown
   */
  private ImagePojo saveImage(ImagePojo inputPojo) throws BadInputException {
    try {
      return imagesCrudService.create(inputPojo);
    } catch (EntityAlreadyExistsException ex) {
      Optional<Image> existing = imagesCrudService.getExisting(inputPojo);
      if (existing.isPresent()) {
        return imageConverter.convertToPojo(existing.get());
      } else {
        throw new RuntimeException("Persistence context is in a wrong state - Existing image couldn't be found");
      }
    }
  }
}
