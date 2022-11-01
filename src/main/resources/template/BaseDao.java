package %packageName%.dao.base;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import %packageName%.parameter.SearchParameter;

public interface BaseDao<T, ID extends Serializable> {

  default List<T> selectList() {
    return selectList(null);
  }

  public List<T> selectList(SearchParameter searchParameter);

  public T selectOne(@Param("id") final ID id);

  public T selectOneByParam(SearchParameter searchParameter);

  public long selectListCount(SearchParameter searchParameter);

  default long selectListCount() {
    return selectListCount(null);
  }

  public int insert(@Param("entity") T t);

  public int update(@Param("entity") T t);

  /**
   * 물리적 삭제처리
   *
   * @param id
   * @param prettyLog
   * @return
   */
  public int delete(@Param("id") ID id);

  /**
   * 논리적 삭제 처리
   *
   * @param id
   * @param delId
   * @param prettyLog
   * @return
   */
  public int delete(@Param("id") ID id, @Param("delId") String delId);
}
