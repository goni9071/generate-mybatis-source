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

public class Mysql {

  private static Logger logger = LoggerFactory.getLogger(Mysql.class);

  public static List<DbColumn> getColumns(final String tableName, String dbUrl, String dbId, String dbPwd) {
    // final String sql = "SHOW COLUMNS FROM " + tableName;
    final String sql = "SHOW FULL COLUMNS FROM " + tableName;
    return (List<DbColumn>) new Transaction(sql, dbUrl, dbId, dbPwd, DriverClass.MYSQL) {
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
            column.setColumnName(rs.getString(1));
            column.setDataType(rs.getString(2));
            column.setNullable(rs.getString(4));
            column.setConstrainst(rs.getString(5));
            column.setDefaultValue(rs.getString(6));
            column.setExtra(rs.getString(7));
            column.setComments(rs.getString(9));

            logger.info(column.toString());
            resultList.add(column);
          }
        } catch (SQLException e) {
          logger.error("SQL ERROR", e);
        }
        return resultList;
      }
    }.getResultList(DbColumn.class);
  }

  public static String getTableComment(final String tableName, String dbUrl, String dbId, String dbPwd) {
    final String sql = "SHOW TABLE STATUS WHERE Name='" + tableName + "'";
    return (String) new Transaction(sql, dbUrl, dbId, dbPwd, DriverClass.MYSQL) {
      @Override
      public Object doTransaction(Connection con) {
        String result = null;
        try {
          {
            Statement stmt = null;
            ResultSet rs = null;
            try {
              stmt = con.createStatement();
              rs = stmt.executeQuery(sql);
            } catch (SQLException e) {
              logger.error(sql, e);
            }

            while (rs.next()) {
              result = rs.getString("Comment");
              logger.info(result);
            }
          }
        } catch (SQLException e) {
          logger.error("SQL ERROR", e);
        }
        return result;
      }
    }.getResult(String.class);
  }

  public static List<DbColumn> getTableList(String url, String id, String pwd) {
    final String sql = "show tables";
    return (List<DbColumn>) new Transaction(sql, url, id, pwd, DriverClass.MYSQL) {
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
            resultList.add(column);
            logger.info(column.toString());
          }
          System.out.println(resultList);
        } catch (SQLException e) {
          logger.error("SQL ERROR", e);
        }
        return resultList;
      }
    }.getResultList(DbColumn.class);
  }

}
