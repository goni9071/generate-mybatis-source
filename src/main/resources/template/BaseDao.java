package %packageName%.dao.base;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import %packageName%.util.PrettyLog;

public interface BaseDao<T, ID extends Serializable> {
  default List<T> selectList(PrettyLog prettyLog) {
    return selectList(null, null, prettyLog);
  }

  default List<T> selectList(@Param("pageable") CustomPageable pageable, PrettyLog prettyLog) {
    return selectList(null, pageable, prettyLog);
  }

  default List<T> selectList(@Param("scParam") SearchParameter searchParameter, PrettyLog prettyLog) {
    return selectList(searchParameter, null, prettyLog);
  }

  public List<T> selectList(@Param("scParam") SearchParameter searchParameter,
      @Param("pageable") CustomPageable pageable, PrettyLog prettyLog);

  public T selectOne(@Param("id") final ID id, PrettyLog prettyLog);

  public T selectOne(@Param("scParam") SearchParameter searchParameter, PrettyLog prettyLog);

  public long selectListCount(@Param("scParam") SearchParameter searchParameter, PrettyLog prettyLog);

  default long selectListCount(PrettyLog prettyLog) {
    return selectListCount(null, prettyLog);
  }

  public int insert(@Param("entity") T t, PrettyLog prettyLog);

  public int update(@Param("entity") T t, PrettyLog prettyLog);

  /**
   * 물리적 삭제처리
   * 
   * @param id
   * @param prettyLog
   * @return
   */
  public int delete(@Param("id") ID id, PrettyLog prettyLog);

  /**
   * 논리적 삭제 처리
   * 
   * @param id
   * @param delId
   * @param prettyLog
   * @return
   */
  public int delete(@Param("id") ID id, @Param("delId") String delId, PrettyLog prettyLog);
}
