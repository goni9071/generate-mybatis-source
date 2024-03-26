package com.maumjido.generate.mybatis.source.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Connect {
  private static Logger logger = LoggerFactory.getLogger(Transaction.class);

  public enum DriverClass {
    MARIA("org.mariadb.jdbc.Driver"), //
    ORACLE("oracle.jdbc.driver.OracleDriver"), //
    MSSQL("com.microsoft.sqlserver.jdbc.SQLServerDriver"), //
    CUBRID("cubrid.jdbc.driver.CUBRIDDriver"), //
    MYSQL("com.mysql.cj.jdbc.Driver"), //
    TIBERO("com.tmax.tibero.jdbc.TbDriver"), //
    DB2("com.ibm.db2.jcc.DB2Driver"),
    POSTGRE("org.postgresql.Driver"), //
    ;

    private String name;

    DriverClass(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }
  }

  public static Connection getConnection(String url, String id, String pwd, DriverClass dirverClass) {
    Connection con = null;
    try {
      Class.forName(dirverClass.getName());
    } catch (ClassNotFoundException ex) {
      logger.error("JDBC  ClassNotFoundException! : {}", ex.getMessage());
      ex.printStackTrace();
    }

    try {
      con = DriverManager.getConnection(url, id, pwd);

    } catch (SQLException sqex) {
      logger.error("SQLException: {}", sqex.getMessage());
      logger.error("SQLState: {}", sqex.getSQLState());
    }
    return con;
  }
}
