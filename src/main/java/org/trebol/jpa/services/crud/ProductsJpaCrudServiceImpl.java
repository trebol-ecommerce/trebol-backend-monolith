package org.trebol.jpa.services.crud;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.exceptions.BadInputException;
import org.trebol.exceptions.EntityAlreadyExistsException;
import org.trebol.jpa.entities.Image;
import org.trebol.jpa.entities.Product;
import org.trebol.jpa.entities.ProductImage;
import org.trebol.jpa.repositories.IImagesJpaRepository;
import org.trebol.jpa.repositories.IProductImagesJpaRepository;
import org.trebol.jpa.repositories.IProductsJpaRepository;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.ImagePojo;
import org.trebol.pojo.ProductPojo;

import javax.validation.Validator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Transactional
@Service
public class ProductsJpaCrudServiceImpl
  extends GenericCrudJpaService<ProductPojo, Product> {

  private final IProductsJpaRepository productsRepository;
  private final IImagesJpaRepository imagesRepository;
  private final IProductImagesJpaRepository productImagesRepository;
  private final ITwoWayConverterJpaService<ImagePojo, Image> imageConverter;
  private final Validator validator;

  @Autowired
  public ProductsJpaCrudServiceImpl(IProductsJpaRepository repository,
                                    IImagesJpaRepository imagesRepository,
                                    IProductImagesJpaRepository productImagesRepository,
                                    ITwoWayConverterJpaService<ProductPojo, Product> converter,
                                    ITwoWayConverterJpaService<ImagePojo, Image> imageConverter,
                                    Validator validator) {
    super(repository,
          converter,
          LoggerFactory.getLogger(ProductsJpaCrudServiceImpl.class));
    this.productsRepository = repository;
    this.imagesRepository = imagesRepository;
    this.productImagesRepository = productImagesRepository;
    this.imageConverter = imageConverter;
    this.validator = validator;
  }

  @Override
  public ProductPojo create(ProductPojo inputPojo) throws BadInputException, EntityAlreadyExistsException {
    if (this.itemExists(inputPojo)) {
      throw new EntityAlreadyExistsException("The item to be created already exists");
    } else {
      Product input = converter.convertToNewEntity(inputPojo);
      Product output = repository.saveAndFlush(input);
      ProductPojo result = converter.convertToPojo(output);
      this.applyImages(inputPojo, output);
      return result;
    }
  }

  @Override
  public Optional<Product> getExisting(ProductPojo input) throws BadInputException {
    String barcode = input.getBarcode();
    if (barcode == null || barcode.isEmpty()) {
      throw new BadInputException("Invalid product barcode");
    } else {
      return this.productsRepository.findByBarcode(barcode);
    }
  }

  private void applyImages(ProductPojo source, Product target) throws BadInputException {
    Collection<ImagePojo> images = source.getImages();
    if (images != null) {
      List<ProductImage> targetImages = new ArrayList<>();
      for (ImagePojo img : images) {
        if (validator.validate(img).isEmpty()) {
          String filename = img.getFilename();
          Image image;
          Optional<Image> filenameMatch = imagesRepository.findByFilename(filename);
          if (filenameMatch.isEmpty()) {
            image = imageConverter.convertToNewEntity(img);
            if (image != null) {
              image = imagesRepository.saveAndFlush(image);
            }
          } else {
            image = filenameMatch.get();
          }
          ProductImage targetImage = new ProductImage();
          targetImage.setProduct(target);
          targetImage.setImage(image);
          targetImages.add(targetImage);
        }
      }

      if (target.getId() != null) {
        productImagesRepository.deleteByProductId(target.getId());
      }
      if (!targetImages.isEmpty()) {
        productImagesRepository.saveAll(targetImages);
      }
    }
  }
}
