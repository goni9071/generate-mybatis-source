package com.maumjido.generate.mybatis.source.dbvendors;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maumjido.generate.mybatis.source.db.Connect.DriverClass;
import com.maumjido.generate.mybatis.source.db.DbColumn;
import com.maumjido.generate.mybatis.source.db.Transaction;

public class Cubrid {

  private static Logger logger = LoggerFactory.getLogger(Cubrid.class);

  public static List<DbColumn> getColumns(String tableName, String dbUrl, String dbId, String dbPwd) {
    final String sql = "SELECT  ik.index_name     AS index_nm,ik.is_primary_key AS is_primary_key,dc.class_name     AS table_nm,da.attr_name      AS column_nm ,da.def_order      AS column_order,da.data_type      AS data_type  ,da.prec           AS data_precision      ,da.scale          AS data_scale       ,da.is_nullable    AS nullable       ,da.default_value  AS data_default FROM   db_class dc    INNER JOIN db_attribute da             ON   dc.class_name = da.class_name       LEFT OUTER JOIN (SELECT  ik.key_attr_name                               ,ik.key_order                               ,ix.index_name                               ,ix.is_unique                               ,ix.class_name                               ,ix.is_primary_key                          FROM db_index_key ik                               INNER JOIN DB_INDEX ix                                       ON ik.index_name = ix.index_name                       ) ik ON   ik.class_name    = da.class_name      AND  ik.key_attr_name = da.attr_name       AND ik.class_name = '"
        + tableName + "' WHERE dc.class_name='" + tableName + "' AND da.class_name='" + tableName + "'";

    return (List<DbColumn>) new Transaction(sql, dbUrl, dbId, dbPwd, DriverClass.CUBRID) {
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
            // if (logger.isDebugEnabled()) {
            logger.info("1:{}, 2:{}, 3:{}, 4:{}, 5:{}, 6:{}", new Object[] { rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6) });
            // }

            column.setColumnName(rs.getString(4));
            column.setDataType(rs.getString(6));
            column.setNullable(rs.getString(5));
            column.setConstrainst(rs.getString(2));
            if (logger.isDebugEnabled()) {
              logger.debug(column.toString());
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
    final String sql = "SELECT * FROM db_class";
    return (List<DbColumn>) new Transaction(sql, url, id, pwd, DriverClass.CUBRID) {
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
            // column.setTableComments(rs.getString(2));
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
