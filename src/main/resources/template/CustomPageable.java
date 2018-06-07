package %packageName%.dao.base;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public class CustomPageable extends PageRequest implements Pageable {

  public static final int DEFAULT_PAGE_NUMBER = 0;
  public static final int DEFAULT_PAGE_SIZE = 10;
  /**
   * 
   */
  private static final long serialVersionUID = -8841888186692528689L;
  private boolean isMysql = true;

  public CustomPageable() {
    super(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
  }

  /**
   * Creates a new {@link PageRequest}. Pages are zero indexed, thus providing 0
   * for {@code page} will return the first page.
   * 
   * @param page
   *          zero-based page index.
   * @param size
   *          the size of the page to be returned.
   */
  public CustomPageable(int page, int size) {
    super(page, size);
  }

  public CustomPageable(int page, int size, Direction direction, String... properties) {
    super(page, size, new Sort(direction, properties));
  }

  public CustomPageable(int page, int size, Sort sort) {
    super(page, size, sort);
  }

  public int getStart() {
    return getOffset() + (isMysql ? 0 : 1);
  }

  public int getEnd() {
    return isMysql ? getPageSize() : getOffset() + getPageSize();
  }

  public boolean isMysql() {
    return isMysql;
  }

  public void setMysql(boolean isMysql) {
    this.isMysql = isMysql;
  }
}
