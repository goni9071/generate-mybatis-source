package %packageName%.dao.base;

import java.util.ArrayList;
import java.util.List;

import %packageName%.util.IfUtil;

public class Page<T> {

  private final long total;

  private List<T> content = new ArrayList<T>();

  private int number;
  private int size;
  private Integer orderType;

  public Page(List<T> content, CustomPageable pageable, long total) {
    this.content.addAll(content);
    this.total = total;
    this.number = pageable == null ? CustomPageable.DEFAULT_PAGE_NUMBER
        : IfUtil.nvl(pageable.getOffset(), CustomPageable.DEFAULT_PAGE_NUMBER);
    this.size = pageable == null ? CustomPageable.DEFAULT_PAGE_SIZE
        : IfUtil.nvl(pageable.getPageSize(), CustomPageable.DEFAULT_PAGE_SIZE);
    this.orderType = pageable == null ? null : pageable.getOrderType();
  }

  public Page(List<T> content) {
    this(content, null, null == content ? 0 : content.size());
  }

  public List<T> getContent() {
    return content;
  }

  public int getTotalPages() {
    return getSize() == 0 ? 1 : (int) Math.ceil((double) total / (double) getSize());
  }

  public int getSize() {
    return this.size;
  }

  public long getTotalElements() {
    return total;
  }

  public boolean hasNext() {
    return getNumber() + 1 < getTotalPages();
  }

  @Override
  public String toString() {

    String contentType = "UNKNOWN";
    List<T> content = getContent();

    if (content.size() > 0) {
      contentType = content.get(0).getClass().getName();
    }

    return String.format("Page %s of %d containing %s instances", getNumber() + 1, getTotalPages(), contentType);
  }

  public int getNumber() {
    return this.number;
  }

  public long getNo() {
    return getTotalElements() - ((getNumber() - 1) * getSize());
  }

  public Integer getOrderType() {
    return orderType;
  }
}
