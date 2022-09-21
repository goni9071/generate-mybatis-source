package %packageName%.parameter;

import %packageName%.dao.base.CustomPageable;
import lombok.Data;

/**
 * 공통 검색조건
 */
@Data
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