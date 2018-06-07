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

public class Mssql {

  private static Logger logger = LoggerFactory.getLogger(Mssql.class);

  public static List<DbColumn> getColumns(final String tableName, String dbUrl, String dbId, String dbPwd) {
    String sql = "SELECT";
    sql += "  A.COLUMN_NAME                                                                     ";
    sql += "  ,A.DATA_TYPE                                                                       ";
    sql += "  ,A.IS_NULLABLE                                                                     ";
    sql += "  ,ISNULL(B.CONSTRAINT_NAME,'')                                                      ";
    sql += "  ,ISNULL(A.COLUMN_DEFAULT,'')                                                       ";
    sql += "  ,A.TABLE_NAME                                                                      ";
    sql += "  ,CAST(CD.VALUE AS VARCHAR(4000))                                                                    ";
    sql += "  ,A.TABLE_CATALOG                                                                    ";
    sql += "  ,A.ORDINAL_POSITION                                                                ";
    sql += "  ,ISNULL(A.CHARACTER_MAXIMUM_LENGTH,'')                                             ";
    sql += "  ,ISNULL(A.NUMERIC_PRECISION,'')                                                    ";
    sql += "  ,ISNULL(A.CHARACTER_SET_NAME,'')                                                   ";
    sql += "  ,ISNULL(A.COLLATION_NAME,'')                                                       ";
    sql += "  ,CASE WHEN ISNULL(C.NAME,'') = '' THEN '' ELSE 'Identity' END auto                 ";
    sql += " FROM                                                                                ";
    sql += "  INFORMATION_SCHEMA.COLUMNS A                                           ";
    sql += "  LEFT OUTER JOIN                                                                    ";
    sql += "  INFORMATION_SCHEMA.KEY_COLUMN_USAGE B                                  ";
    sql += "  ON A.TABLE_NAME = B.TABLE_NAME                                                     ";
    sql += "  AND A.COLUMN_NAME = B.COLUMN_NAME                                                  ";
    sql += "  LEFT OUTER JOIN                                                                    ";
    sql += "  syscolumns C                                                                       ";
    sql += "  ON C.ID = object_id(A.TABLE_NAME) AND A.COLUMN_NAME = C.NAME AND C.COLSTAT & 1 = 1 ";
    sql += "  LEFT OUTER JOIN                                                                  ";
    sql += "  SYS.EXTENDED_PROPERTIES  CD                                                          ";
    sql += "  ON CD.MAJOR_ID = C.ID AND CD.MINOR_ID = C.COLID AND CD.NAME = 'MS_Description'  ";
    sql += " WHERE                                                                               ";
    sql += "  A.TABLE_NAME = '" + tableName + "'                                                          ";
    sql += " ORDER BY A.ORDINAL_POSITION;                                                        ";
    final String sql1 = sql;
    return (List<DbColumn>) new Transaction(sql, dbUrl, dbId, dbPwd, DriverClass.MSSQL) {
      @Override
      public Object doTransaction(Connection con) {
        List<DbColumn> resultList = new ArrayList<DbColumn>();
        try {
          Statement stmt = null;
          ResultSet rs = null;
          try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql1);
          } catch (SQLException e) {
            logger.error(sql1, e);
          }

          while (rs.next()) {
            DbColumn column = new DbColumn();
            column.setColumnName(rs.getString(1));
            column.setDataType(rs.getString(2));
            column.setNullable(rs.getString(3));
            column.setConstrainst(rs.getString(4));
            column.setDefaultValue(rs.getString(5));
            column.setExtra(rs.getString(6));
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
    final StringBuffer sql = new StringBuffer("");
    sql.append("SELECT CAST(TABLE_DESC AS VARCHAR(4000)) AS Comment ");
    sql.append("  FROM INFORMATION_SCHEMA.TABLES T1                                                                                        ");
    sql.append("  LEFT OUTER JOIN (                                                                                                        ");
    sql.append("   SELECT                                                                                                                  ");
    sql.append("   T.NAME TABLE_NAME, TD.VALUE TABLE_DESC                                                                                  ");
    sql.append("   FROM SYSOBJECTS T                                                                                                       ");
    sql.append("   INNER JOIN SYSUSERS U ON U.UID = T.UID                                                                                  ");
    sql.append("   LEFT OUTER JOIN SYS.EXTENDED_PROPERTIES TD ON TD.MAJOR_ID = T.ID AND TD.MINOR_ID = 0 AND TD.NAME = 'MS_Description'     ");
    sql.append("   WHERE T.TYPE = 'u'                                                                                                      ");
    sql.append("  ) T2 ON T2.TABLE_NAME=T1.TABLE_NAME                                                                                      ");
    sql.append("  WHERE T1.TABLE_NAME = '" + tableName + "'");
    return (String) new Transaction(sql.toString(), dbUrl, dbId, dbPwd, DriverClass.MSSQL) {
      @Override
      public Object doTransaction(Connection con) {
        String result = null;
        try {
          {
            Statement stmt = null;
            ResultSet rs = null;
            try {
              stmt = con.createStatement();
              rs = stmt.executeQuery(sql.toString());
            } catch (SQLException e) {
              logger.error(sql.toString(), e);
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
    final String sql = "SELECT TABLE_NAME FROM  INFORMATION_SCHEMA.TABLES;";
    return (List<DbColumn>) new Transaction(sql, url, id, pwd, DriverClass.MSSQL) {
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
