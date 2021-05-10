package org.trebol.jpa.entities;

import org.trebol.jpa.GenericEntity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
@Entity
@Table(name = "product_images")
@NamedQueries({
    @NamedQuery(name = "ProductImage.findAll", query = "SELECT p FROM ProductImage p")})
public class ProductImage
    implements GenericEntity<Integer> {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "product_image_id")
  private Integer id;
  @JoinColumn(name = "image_id", referencedColumnName = "image_id", insertable = true, updatable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private Image image;
  @JoinColumn(name = "product_id", referencedColumnName = "product_id", insertable = true, updatable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private Product product;

  public ProductImage() {
  }

  public ProductImage(Integer productImageId) {
      this.id = productImageId;
  }

  public Integer getId() {
      return id;
  }

  public void setId(Integer id) {
      this.id = id;
  }

  public Image getImage() {
      return image;
  }

  public void setImage(Image image) {
      this.image = image;
  }

  public Product getProduct() {
      return product;
  }

  public void setProduct(Product product) {
      this.product = product;
  }

  @Override
  public int hashCode() {
      int hash = 0;
      hash += (id != null ? id.hashCode() : 0);
      return hash;
  }

  @Override
  public boolean equals(Object object) {
      // TODO: Warning - this method won't work in the case the id fields are not set
      if (!(object instanceof ProductImage)) {
          return false;
      }
      ProductImage other = (ProductImage) object;
      if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
          return false;
      }
      return true;
  }

  @Override
  public String toString() {
      return "org.trebol.jpa.entities.ProductImage[ id=" + id + " ]";
  }

}
