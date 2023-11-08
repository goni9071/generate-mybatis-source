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

public class Postgre {

  private static Logger logger = LoggerFactory.getLogger(Postgre.class);
  private static final String DEFAULT_SCHEMA = "vus";

  public static List<DbColumn> getColumns(final String tableName, String dbUrl, String dbId, String dbPwd) {
    final String sql = "SELECT "//
        + "    f.attname AS column_name, "//
        + "    pg_catalog.format_type(f.atttypid,f.atttypmod) AS column_type, " + "    CASE "//
        + "        WHEN f.attnotnull THEN 'NO' "//
        + "        ELSE 'YES' "//
        + "    END AS nullable, "//
        + "    pg_catalog.pg_get_expr(d.adbin, d.adrelid) AS default_value, "//
        + "    p.conname AS constraint_name, "//
        + "    p.contype AS constraint_type, "//
        + "    pg_catalog.col_description(c.oid, f.attnum) AS column_comment "//
        + "FROM "//
        + "    pg_attribute f "//
        + "    JOIN pg_class c ON c.oid = f.attrelid "//
        + "    JOIN pg_type t ON t.oid = f.atttypid "//
        + "    LEFT JOIN pg_attrdef d ON d.adrelid = c.oid AND d.adnum = f.attnum "//
        + "    LEFT JOIN pg_namespace n ON n.oid = c.relnamespace "//
        + "    LEFT JOIN pg_constraint p ON p.conrelid = c.oid AND f.attnum = ANY (p.conkey) "//
        + "WHERE "//
        + "    c.relkind IN ('r', 'm') "//
        + "    AND n.nspname = '" + DEFAULT_SCHEMA + "'"// 기본 public 처리.
        + "    AND c.relname = '" + tableName + "' "//
        + "    AND f.attnum > 0 "//
        + "ORDER BY f.attnum"//
        + "";
    return (List<DbColumn>) new Transaction(sql, dbUrl, dbId, dbPwd, DriverClass.POSTGRE) {
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
            column.setNullable(rs.getString(3));
            column.setConstrainst(rs.getString(6));
            column.setDefaultValue(rs.getString(4));
            column.setExtra(rs.getString(5));
            column.setComments(rs.getString(7));

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
    final String sql = "SELECT "//
        + "       pgd.description AS Comment "//
        + "FROM pg_class c "//
        + "JOIN pg_description pgd ON pgd.objoid = c.oid "//
        + "WHERE pgd.objsubid = 0  "//
        + "  AND (c.relkind = 'r' OR c.relkind = 'm')" // 'r' for regular table, 'm' for materialized view
        + "  AND c.relname = '" + tableName + "'";//
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
    final String sql = "SELECT tablename "//
        + "FROM pg_catalog.pg_tables "//
        + "WHERE schemaname = '" + DEFAULT_SCHEMA + "'"//
        + " UNION "//
        + "SELECT matviewname AS tablename "//
        + "FROM pg_catalog.pg_matviews " //
        + "WHERE schemaname = '" + DEFAULT_SCHEMA + "'"//
    ;
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
