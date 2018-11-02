package com.maumjido.generate.mybatis.source.dbvendors;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maumjido.generate.mybatis.source.db.DbColumn;
import com.maumjido.generate.mybatis.source.db.Transaction;
import com.maumjido.generate.mybatis.source.db.Connect.DriverClass;

public class Tibero {

  private static Logger logger = LoggerFactory.getLogger(Tibero.class);

  public static List<DbColumn> getColumns(String tableName, String dbUrl, String dbId, String dbPwd) {
    // final String sql = "SHOW COLUMNS FROM " + tableName;
    final String sql = "SELECT tab_columns.TABLE_NAME, tab_columns.COLUMN_ID, tab_columns.COLUMN_NAME, (case when DATA_TYPE like '%CHAR%' then DATA_TYPE || '(' || DATA_LENGTH || ')' when DATA_TYPE = 'NUMBER' and DATA_PRECISION > 0 and DATA_SCALE > 0 then DATA_TYPE || '(' || DATA_PRECISION || ',' || DATA_SCALE || ')'when DATA_TYPE = 'NUMBER' and DATA_PRECISION > 0 then DATA_TYPE || '(' || DATA_PRECISION || ')'when DATA_TYPE = 'NUMBER' then DATA_TYPE else DATA_TYPE end) DATA_TYPE, decode(NULLABLE, 'N', 'Not Null', 'Null') NULLABLE, DATA_DEFAULT, (SELECT decode(sum((SELECT decode(CONSTRAINT_TYPE, 'P', 1, 'R', 2, 0)FROM USER_CONSTRAINTS  WHERE CONSTRAINT_NAME = cons_columns.CONSTRAINT_NAME)), 1, 'PK', 2, 'FK', 3, 'PK, FK', '') FROM USER_CONS_COLUMNS cons_columns   WHERE TABLE_NAME = tab_columns.TABLE_NAME AND COLUMN_NAME = tab_columns.COLUMN_NAME) CONSTRAINTS, comments.COMMENTS , TAB.COMMENTS as tableComment FROM USER_TAB_COLUMNS tab_columns, USER_COL_COMMENTS comments , ALL_TAB_COMMENTS TAB WHERE tab_columns.TABLE_NAME = comments.TABLE_NAME(+) AND tab_columns.TABLE_NAME = TAB.TABLE_NAME AND tab_columns.COLUMN_NAME = comments.COLUMN_NAME(+) AND tab_columns.TABLE_NAME = '"
        + tableName + "' ORDER BY tab_columns.TABLE_NAME, COLUMN_ID";

    return (List<DbColumn>) new Transaction(sql, dbUrl, dbId, dbPwd, DriverClass.TIBERO) {
      @Override
      public Object doTransaction(Connection con) {
        List<DbColumn> resultList = new ArrayList<DbColumn>();
        try {
          Statement stmt = null;
          ResultSet rs = null;
          try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
          } catch (SQLException e) {
            logger.error(sql, e);
          }

          while (rs.next()) {
            DbColumn column = new DbColumn();
            Map<String, String> result = new HashMap<String, String>();
            if (logger.isDebugEnabled()) {
              logger.debug("1:{}, 2:{}, 3:{}, 4:{}, 5:{}, 6:{}", new Object[] { rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6) });
            }
            column.setTableName(rs.getString(1));
            column.setColumnId(rs.getString(2));
            column.setColumnName(rs.getString(3));
            column.setDataType(rs.getString(4));
            column.setNullable(rs.getString(5));
            column.setDefaultValue(rs.getString(6));
            column.setConstrainst(rs.getString(7));
            column.setComments(rs.getString(8));
            column.setTableComments(rs.getString(9));
            if (logger.isDebugEnabled()) {
              logger.debug(result.toString());
            }
            resultList.add(column);
          }
        } catch (SQLException e) {
          logger.error("SQL ERROR", e);
        }
        return resultList;
      }
    }.getResultList(DbColumn.class);
  }

  public static List<DbColumn> getTableList(String url, String id, String pwd) {
    logger.info("DB Table List 조회");
    final String sql = "SELECT b.TABLE_NAME, b.comments FROM USER_TABLES a LEFT OUTER JOIN all_tab_comments b on a.table_name = b.table_name WHERE b.table_type='TABLE'";
    return (List<DbColumn>) new Transaction(sql, url, id, pwd, DriverClass.TIBERO) {
      @Override
      public Object doTransaction(Connection con) {
        List<DbColumn> resultList = new ArrayList<DbColumn>();
        try {
          Statement stmt = null;
          ResultSet rs = null;
          try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
          } catch (SQLException e) {
            logger.error(sql, e);
          }
          while (rs.next()) {
            DbColumn column = new DbColumn();
            column.setTableName(rs.getString(1));
            column.setTableComments(rs.getString(2));
            resultList.add(column);
          }
          if (logger.isDebugEnabled()) {
            logger.debug("Table List: {}", resultList);
          }
        } catch (SQLException e) {
          logger.error("SQL ERROR", e);
        }
        return resultList;
      }
    }.getResultList(DbColumn.class);
  }

}
