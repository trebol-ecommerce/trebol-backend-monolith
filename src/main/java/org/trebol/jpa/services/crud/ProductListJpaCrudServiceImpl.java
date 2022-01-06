package org.trebol.jpa.services.crud;

import com.querydsl.core.types.Predicate;
import javassist.NotFoundException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.jpa.entities.ProductList;
import org.trebol.jpa.repositories.IProductListItemsJpaRepository;
import org.trebol.jpa.repositories.IProductListsJpaRepository;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.ProductListPojo;

import java.util.Optional;

@Transactional
@Service
public class ProductListJpaCrudServiceImpl
  extends GenericCrudJpaService<ProductListPojo, ProductList> {

  private final IProductListsJpaRepository productListRepository;
  private final IProductListItemsJpaRepository productListItemRepository;

  @Autowired
  public ProductListJpaCrudServiceImpl(IProductListsJpaRepository productListRepository,
                                       IProductListItemsJpaRepository productListItemRepository,
                                       ITwoWayConverterJpaService<ProductListPojo, ProductList> converterService) {
    super(productListRepository,
           converterService,
           LoggerFactory.getLogger(ProductListJpaCrudServiceImpl.class));
    this.productListRepository = productListRepository;
    this.productListItemRepository = productListItemRepository;
  }

  @Override
  public void delete(Predicate filters) throws NotFoundException {
    long count = productListRepository.count(filters);
    if (count == 0) {
      throw new NotFoundException(ITEM_NOT_FOUND);
    } else {
      for (ProductList list : productListRepository.findAll(filters)) {
        productListItemRepository.deleteByListId(list.getId());
      }

      productListRepository.deleteAll(productListRepository.findAll(filters));
    }
  }

  @Override
  public Optional<ProductList> getExisting(ProductListPojo input) {
    Long id = input.getId();
    String name = input.getName();
    if (id == null && name == null) {
      return Optional.empty();
    } else if (id != null) {
      return productListRepository.findById(id);
    } else {
      return productListRepository.findByName(name);
    }
  }


}
