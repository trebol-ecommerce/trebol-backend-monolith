package cl.blm.newmarketing.backend.dtos;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
public class SellTypeDto {
  private Integer sellTypeId;
  private String sellTypeName;

  public SellTypeDto() {
    super();
  }

  public Integer getSellTypeId() {
    return sellTypeId;
  }

  public void setSellTypeId(Integer sellTypeId) {
    this.sellTypeId = sellTypeId;
  }

  public String getSellTypeName() {
    return sellTypeName;
  }

  public void setSellTypeName(String sellTypeName) {
    this.sellTypeName = sellTypeName;
  }
}
