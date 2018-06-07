package com.maumjido.generate.mybatis.source.generate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maumjido.generate.mybatis.source.db.DbColumn;
import com.maumjido.generate.mybatis.source.dbvendors.Cubrid;
import com.maumjido.generate.mybatis.source.dbvendors.Mssql;
import com.maumjido.generate.mybatis.source.dbvendors.Mysql;
import com.maumjido.generate.mybatis.source.dbvendors.Oracle;

public class MybatisGenerate {

  private static Logger logger = LoggerFactory.getLogger(MybatisGenerate.class);
  public static final String TARGET_PATH = "result/";

  public static String getDbType(String dbUrl) {
    return dbUrl.replaceFirst("^jdbc:([^:]+):.*", "$1");
  }

  public static void generate(String packageName, String dbUrl, String dbId, String dbPwd, String currentProjectPath) throws IOException {
    String dbType = getDbType(dbUrl);

    generate(packageName, dbUrl, dbId, dbPwd, currentProjectPath, dbType);
  }

  public static void generate(String packageName, String dbUrl, String dbId, String dbPwd, String currentProjectPath, String dbType) throws IOException {

    String basePath = TARGET_PATH + "src\\main\\java\\" + packageName.replaceAll("\\.", "\\\\");

    String entityFilePath = basePath + "\\entity";
    String daoFilePath = basePath + "\\dao";
    String sqlFilePath = TARGET_PATH + "\\src\\main\\resources\\common\\sql";

    GenerateDao.createDaoStaticTemplate(daoFilePath + "\\base", packageName);

    if ("oracle".equals(dbType)) {
      oracle(packageName, dbUrl, dbId, dbPwd, currentProjectPath, entityFilePath, daoFilePath, sqlFilePath);
    } else if ("cubrid".equals(dbType)) {
      cubrid(packageName, dbUrl, dbId, dbPwd, currentProjectPath, entityFilePath, daoFilePath, sqlFilePath);
    } else if ("mysql".equals(dbType) || "mariadb".equals(dbType)) {
      mysql(packageName, dbUrl, dbId, dbPwd, currentProjectPath, entityFilePath, daoFilePath, sqlFilePath);
    } else if ("sqlserver".equals(dbType)) {
      mssql(packageName, dbUrl, dbId, dbPwd, currentProjectPath, entityFilePath, daoFilePath, sqlFilePath);
    } else {
      logger.info("{} 지원하지 않는 jdbc url 입니다. ", dbUrl);
    }

  }

  private static void mysql(String packageName, String dbUrl, String dbId, String dbPwd, String currentProjectPath, String entityFilePath, String daoFilePath, String sqlFilePath) throws IOException {
    // TableList 조회
    List<DbColumn> tableList = Mysql.getTableList(dbUrl, dbId, dbPwd);
    logger.info("---------------------Create mysql mybatis-------------------------");
    String content = GenerateSql.getMybatisMapperConfig(packageName + ".entity", "sql", tableList);
    GenerateConfig.generateConfigFile(packageName, content);
    for (DbColumn table : tableList) {
      String tableName = table.getTableName();
      logger.info("{} 테이블 관련 GenerateEntity, GenerateDao, Sql 생성 ", tableName);

      List<DbColumn> columns = Mysql.getColumns(tableName, dbUrl, dbId, dbPwd);
      String tableComment = Mysql.getTableComment(tableName, dbUrl, dbId, dbPwd);
      GenerateEntity.create(entityFilePath, tableName, columns, tableComment);
      GenerateDao.create(daoFilePath, tableName, columns);

      GenerateSql.create(sqlFilePath, packageName + ".dao", tableName, columns, tableComment);
    }
  }

  private static void mssql(String packageName, String dbUrl, String dbId, String dbPwd, String currentProjectPath, String entityFilePath, String daoFilePath, String sqlFilePath) throws IOException {
    // TableList 조회
    List<DbColumn> tableList = Mssql.getTableList(dbUrl, dbId, dbPwd);
    logger.info("---------------------Create mssql mybatis-------------------------");
    String content = GenerateSql.getMybatisMapperConfig(packageName + ".entity", "sql", tableList);

    GenerateConfig.generateConfigFile(packageName, content);
    for (DbColumn table : tableList) {
      String tableName = table.getTableName();
      logger.info("{} 테이블 관련 GenerateEntity, GenerateDao, Sql 생성 ", tableName);

      List<DbColumn> columns = Mssql.getColumns(tableName, dbUrl, dbId, dbPwd);
      String tableComment = Mssql.getTableComment(tableName, dbUrl, dbId, dbPwd);
      GenerateEntity.create(entityFilePath, tableName, columns, tableComment);
      GenerateDao.create(daoFilePath, tableName, columns);

      GenerateSql.create(sqlFilePath, packageName + ".dao", tableName, columns, tableComment);
    }
  }

  private static void oracle(String packageName, String dbUrl, String dbId, String dbPwd, String currentProjectPath, String entityFilePath, String daoFilePath, String sqlFilePath)
      throws IOException, UnsupportedEncodingException {
    // TableList 조회
    List<DbColumn> tableList = Oracle.getTableList(dbUrl, dbId, dbPwd);
    logger.info("---------------------Create mybatis-------------------------");
    String content = GenerateSql.getMybatisMapperConfig(packageName + ".entity", "sql", tableList);

    GenerateConfig.generateConfigFile(packageName, content);
    for (DbColumn table : tableList) {
      String tableName = table.getTableName();
      String tableComments = table.getTableComments();
      logger.info("{} 테이블 관련 GenerateEntity, GenerateDao, Sql 생성 ", tableName);

      List<DbColumn> columns = Oracle.getColumns(tableName, dbUrl, dbId, dbPwd);
      GenerateEntity.create(entityFilePath, tableName, columns, tableComments);
      GenerateDao.create(daoFilePath, tableName, columns);
      GenerateSql.create(sqlFilePath, packageName + ".dao", tableName, columns, tableComments);
    }
  }

  private static void cubrid(String packageName, String dbUrl, String dbId, String dbPwd, String currentProjectPath, String entityFilePath, String daoFilePath, String sqlFilePath)
      throws IOException, UnsupportedEncodingException {

    // TableList 조회
    List<DbColumn> tableList = Cubrid.getTableList(dbUrl, dbId, dbPwd);
    logger.info("---------------------Create mybatis-------------------------");
    String content = GenerateSql.getMybatisMapperConfig(packageName + ".entity", "sql", tableList);

    GenerateConfig.generateConfigFile(packageName, content);
    for (DbColumn table : tableList) {
      String tableName = table.getTableName();
      String tableComments = table.getTableComments();
      logger.info("{} 테이블 관련 GenerateEntity, GenerateDao, Sql 생성 ", tableName);

      List<DbColumn> columns = Cubrid.getColumns(tableName, dbUrl, dbId, dbPwd);
      GenerateEntity.create(entityFilePath, tableName, columns, tableComments);
      GenerateDao.create(daoFilePath, tableName, columns);
      GenerateSql.create(sqlFilePath, packageName + ".dao", tableName, columns, tableComments);
    }

  }

}
