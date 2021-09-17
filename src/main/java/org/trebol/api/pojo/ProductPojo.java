package org.trebol.api.pojo;

import java.util.Collection;
import java.util.Objects;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@JsonInclude(NON_NULL)
public class ProductPojo {
  @JsonIgnore
  private Long id;
  @NotBlank
  private String name;
  @NotBlank
  private String barcode;
  @NotNull
  private Integer price;
  private ProductCategoryPojo category;
  @JsonInclude(NON_EMPTY)
  private String description;
  @JsonIgnore
  private Integer currentStock;
  @JsonIgnore
  private Integer criticalStock;
  private Collection<ImagePojo> images;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getBarcode() {
    return barcode;
  }

  public void setBarcode(String barcode) {
    this.barcode = barcode;
  }

  public Integer getPrice() {
    return price;
  }

  public void setPrice(Integer price) {
    this.price = price;
  }

  public ProductCategoryPojo getCategory() {
    return category;
  }

  public void setCategory(ProductCategoryPojo category) {
    this.category = category;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Integer getCurrentStock() {
    return currentStock;
  }

  public void setCurrentStock(Integer currentStock) {
    this.currentStock = currentStock;
  }

  public Integer getCriticalStock() {
    return criticalStock;
  }

  public void setCriticalStock(Integer criticalStock) {
    this.criticalStock = criticalStock;
  }

  public Collection<ImagePojo> getImages() {
    return images;
  }

  public void setImages(Collection<ImagePojo> images) {
    this.images = images;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 29 * hash + Objects.hashCode(this.id);
    hash = 29 * hash + Objects.hashCode(this.name);
    hash = 29 * hash + Objects.hashCode(this.barcode);
    hash = 29 * hash + Objects.hashCode(this.price);
    hash = 29 * hash + Objects.hashCode(this.category);
    hash = 29 * hash + Objects.hashCode(this.description);
    hash = 29 * hash + Objects.hashCode(this.currentStock);
    hash = 29 * hash + Objects.hashCode(this.criticalStock);
    hash = 29 * hash + Objects.hashCode(this.images);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final ProductPojo other = (ProductPojo)obj;
    if (!Objects.equals(this.name, other.name)) {
      return false;
    }
    if (!Objects.equals(this.barcode, other.barcode)) {
      return false;
    }
    if (!Objects.equals(this.description, other.description)) {
      return false;
    }
    if (!Objects.equals(this.id, other.id)) {
      return false;
    }
    if (!Objects.equals(this.price, other.price)) {
      return false;
    }
    if (!Objects.equals(this.category, other.category)) {
      return false;
    }
    if (!Objects.equals(this.currentStock, other.currentStock)) {
      return false;
    }
    if (!Objects.equals(this.criticalStock, other.criticalStock)) {
      return false;
    }
    if (!Objects.equals(this.images, other.images)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "ProductPojo{id=" + id +
            ", name=" + name +
            ", barcode=" + barcode +
            ", price=" + price +
            ", category=" + category +
            ", description=" + description +
            ", currentStock=" + currentStock +
            ", criticalStock=" + criticalStock +
            ", images=" + images + '}';
  }

}
