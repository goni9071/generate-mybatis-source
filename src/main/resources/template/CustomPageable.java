package %packageName%.dao.base;

import %packageName%.util.IfUtil;

public class CustomPageable {
  private Integer page;
  private Integer size;
  private Integer orderType;
  private boolean oneMore;
  public static final int DEFAULT_PAGE_NUMBER = 1;
  public static final int DEFAULT_PAGE_SIZE = 10;

  public void setSize(int size) {
    this.size = size;
  }

  public void setPage(int page) {
    this.page = page;
  }

  public int getStart() {
    Integer pageSize = IfUtil.nvl(getPageSize(), DEFAULT_PAGE_SIZE);
    Integer offset = IfUtil.nvl(getOffset(), DEFAULT_PAGE_NUMBER);
    return (offset - 1) * pageSize;
  }

  public Integer getOffset() {
    return page;
  }

  public Integer getPage() {
    return IfUtil.nvl(getOffset(), DEFAULT_PAGE_NUMBER);
  }

  public int getEnd() {
    Integer pageSize = IfUtil.nvl(getPageSize(), DEFAULT_PAGE_SIZE);
    return getStart() + pageSize + (oneMore ? 1 : 0);
  }

  public Integer getPageSize() {
    return size;
  }

  public Integer getOrderType() {
    return orderType;
  }

  public void setOrderType(int orderType) {
    this.orderType = orderType;
  }

  public void setOrder(int orderType) {
    this.orderType = orderType;
  }

  public boolean isOneMore() {
    return oneMore;
  }

  public void setOneMore(boolean oneMore) {
    this.oneMore = oneMore;
  }
}
