package %packageName%.dao.base;

import java.util.List;

import ai.withvus.moving.core.dao.base.CustomPageable;

/**
 * 공통 검색조건
 */
public class SearchParameter {
  private int page;
  private int size;
  private CustomPageable pageable;
}
