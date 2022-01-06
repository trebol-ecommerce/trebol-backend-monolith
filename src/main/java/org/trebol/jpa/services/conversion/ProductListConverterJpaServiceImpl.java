package org.trebol.jpa.services.conversion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.ProductList;
import org.trebol.jpa.entities.QProductListItem;
import org.trebol.jpa.repositories.IProductListItemsJpaRepository;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.ProductListPojo;

@Service
public class ProductListConverterJpaServiceImpl
  implements ITwoWayConverterJpaService<ProductListPojo, ProductList> {

  private final IProductListItemsJpaRepository productListItemRepository;

  @Autowired
  public ProductListConverterJpaServiceImpl(IProductListItemsJpaRepository productListItemRepository) {
    this.productListItemRepository = productListItemRepository;
  }

  @Override
  public ProductListPojo convertToPojo(ProductList source) {
    Long sourceListId = source.getId();
    long itemCount = productListItemRepository.count(QProductListItem.productListItem.list.id.eq(sourceListId));
    return new ProductListPojo(sourceListId, source.getName(), source.getCode(), itemCount);
  }

  @Override
  public ProductList convertToNewEntity(ProductListPojo source) throws BadInputException {
    return new ProductList(source.getName(), source.getCode());
  }

  @Override
  public ProductList applyChangesToExistingEntity(ProductListPojo source, ProductList existing) throws BadInputException {
    ProductList target = new ProductList(existing);

    if (source.getName() != null && !source.getName().isEmpty() && !source.getName().equals(target.getName())) {
      target.setName(source.getName());
    }

    if (source.getCode() != null && !source.getCode().isEmpty() && !source.getCode().equals(target.getCode())) {
      target.setCode(source.getCode());
    }

    return target;
  }
}
