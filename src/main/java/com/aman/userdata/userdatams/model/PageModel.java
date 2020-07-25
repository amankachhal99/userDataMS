package com.aman.userdata.userdatams.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.Objects;

@Validated
public class PageModel implements Serializable {
  private static final long serialVersionUID = 1L;

  @JsonProperty("current")
  private Integer current = null;

  @JsonProperty("totalItems")
  private Integer totalItems = null;

  @JsonProperty("size")
  private Integer size = null;

  /**
   * @param current current
   * @return PageModel
   */
  public PageModel current(Integer current) {
    this.current = current;
    return this;
  }

   /**
   * The current page being returned
   * minimum: 0
   * @return current
  **/

@Min(0)
  public Integer getCurrent() {
    return current;
  }
  /**
   * @param current current
   */
  public void setCurrent(Integer current) {
    this.current = current;
  }

  /**
   * @param totalItems totalItems
   * @return PageModel
   */
  public PageModel totalItems(Integer totalItems) {
    this.totalItems = totalItems;
    return this;
  }

   /**
   * The total number of records available
   * minimum: 0
   * @return totalItems
  **/

@Min(0)
  public Integer getTotalItems() {
    return totalItems;
  }
  /**
   * @param totalItems totalItems
   */
  public void setTotalItems(Integer totalItems) {
    this.totalItems = totalItems;
  }

  /**
   * @param size size
   * @return PageModel
   */
  public PageModel size(Integer size) {
    this.size = size;
    return this;
  }

   /**
   * The number of records returned per page
   * minimum: 1
   * maximum: 30
   * @return size
  **/

@Min(1) @Max(30) 
  public Integer getSize() {
    return size;
  }
  /**
   * @param size size
   */
  public void setSize(Integer size) {
    this.size = size;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PageModel pageModel = (PageModel) o;
    return Objects.equals(this.current, pageModel.current) &&
        Objects.equals(this.totalItems, pageModel.totalItems) &&
        Objects.equals(this.size, pageModel.size);
  }

  @Override
  public int hashCode() {
    return Objects.hash(current, totalItems, size);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PageModel {\n");
    
    sb.append("    current: ").append(toIndentedString(current)).append("\n");
    sb.append("    totalItems: ").append(toIndentedString(totalItems)).append("\n");
    sb.append("    size: ").append(toIndentedString(size)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

