package %packageName%.parameter;

import %packageName%.dao.base.CustomPageable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * 공통 검색조건
 */

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SearchParameter {
  private int page;
  private int size;
  private CustomPageable pageable;

  public void setSize(int size) {
    this.size = size;
    if (pageable == null) {
      pageable = new CustomPageable();
    }
    pageable.setSize(size);
  }

  public void setPage(int page) {
    this.page = page;
    if (pageable == null) {
      pageable = new CustomPageable();
    }
    pageable.setPage(page);
  }
}