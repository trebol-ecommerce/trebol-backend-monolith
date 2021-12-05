package org.trebol.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

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

  public ProductPojo() { }

  public ProductPojo(String barcode) {
    this.barcode = barcode;
  }

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
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ProductPojo that = (ProductPojo) o;
    return Objects.equals(id, that.id) &&
        Objects.equals(name, that.name) &&
        Objects.equals(barcode, that.barcode) &&
        Objects.equals(price, that.price) &&
        Objects.equals(category, that.category) &&
        Objects.equals(description, that.description) &&
        Objects.equals(currentStock, that.currentStock) &&
        Objects.equals(criticalStock, that.criticalStock) &&
        Objects.equals(images, that.images);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, barcode, price, category, description, currentStock, criticalStock, images);
  }

  @Override
  public String toString() {
    return "ProductPojo{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", barcode='" + barcode + '\'' +
        ", price=" + price +
        ", category=" + category +
        ", description='" + description + '\'' +
        ", currentStock=" + currentStock +
        ", criticalStock=" + criticalStock +
        ", images=" + images +
        '}';
  }
}
