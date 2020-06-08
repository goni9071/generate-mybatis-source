package com.maumjido.generate.mybatis.source.common;

import com.maumjido.generate.mybatis.source.util.SystemProperties;

public class Constants {
  public static final String PACKAGE_BASE = SystemProperties.getProperty("package.base");
  public static final String DB_URL = SystemProperties.getProperty("db.url");
  public static final String DB_ID = SystemProperties.getProperty("db.id");
  public static final String DB_PWD = SystemProperties.getProperty("db.pwd");
  public static final String INCLUDE_PREFIX_TABLENAME = SystemProperties.getProperty("include.prefix.table_name", "");
  public static final String REMOVE_PREFIX_TABLENAME = SystemProperties.getProperty("remove.prefix.table_name");
  /**
   * Url 코드 사용여부(Y:사용, N:미사용 - 기본:Y)
   */
  public static final String OPTION_USE_URL_CODE = SystemProperties.getProperty("option.use.url_code", "Y");

  public static boolean filter(String tableName) {
    if (INCLUDE_PREFIX_TABLENAME.equals("")) {
      return true;
    }
    for (String prefix : INCLUDE_PREFIX_TABLENAME.split(",")) {
      if (!prefix.trim().equals("") && tableName.startsWith(prefix)) {
        return true;
      }
    }
    return false;
  }
}
