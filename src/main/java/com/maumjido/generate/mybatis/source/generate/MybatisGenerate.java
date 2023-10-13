package com.maumjido.generate.mybatis.source.generate;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maumjido.generate.mybatis.source.common.Constants;
import com.maumjido.generate.mybatis.source.db.DbColumn;
import com.maumjido.generate.mybatis.source.dbvendors.Cubrid;
import com.maumjido.generate.mybatis.source.dbvendors.Mssql;
import com.maumjido.generate.mybatis.source.dbvendors.Mysql;
import com.maumjido.generate.mybatis.source.dbvendors.Oracle;
import com.maumjido.generate.mybatis.source.dbvendors.Postgre;
import com.maumjido.generate.mybatis.source.dbvendors.Tibero;

public class MybatisGenerate {

  private static Logger logger = LoggerFactory.getLogger(MybatisGenerate.class);
  public static final String TARGET_PATH = "result/";

  public static String getDbType(String dbUrl) {
    return dbUrl.replaceFirst("^jdbc:([^:]+):.*", "$1");
  }

  public static void generate(String packageName, String dbUrl, String dbId, String dbPwd, String currentProjectPath)
      throws IOException {
    String dbType = getDbType(dbUrl);

    generate(packageName, dbUrl, dbId, dbPwd, currentProjectPath, dbType);
  }

  public static void generate(String packageName, String dbUrl, String dbId, String dbPwd, String currentProjectPath,
      String dbType) throws IOException {

    String basePath = TARGET_PATH + "src" + File.separator + "main" + File.separator + "java" + File.separator + ""
        + packageName.replaceAll("\\.", File.separator.equals("\\") ? "\\\\" : File.separator);

    String entityFilePath = basePath + File.separator + "entity";
    String daoFilePath = basePath + File.separator + "dao";
    String serviceFilePath = basePath + File.separator + "service";
    String parameterFilePath = basePath + File.separator + "parameter";
    String sqlFilePath = TARGET_PATH + File.separator + "src" + File.separator + "main" + File.separator + "resources"
        + File.separator + "common" + File.separator + "sql";

    GenerateParameter.createParameterStaticTemplate(parameterFilePath, packageName);
    GenerateDao.createDaoStaticTemplate(daoFilePath + "" + File.separator + "base", packageName);
    GenerateService.createServiceStaticTemplate(serviceFilePath + "" + File.separator + "base", packageName);

    if ("oracle".equals(dbType)) {
      oracle(packageName, dbUrl, dbId, dbPwd, currentProjectPath, entityFilePath, daoFilePath, sqlFilePath);
    } else if ("cubrid".equals(dbType)) {
      cubrid(packageName, dbUrl, dbId, dbPwd, currentProjectPath, entityFilePath, daoFilePath, sqlFilePath);
    } else if ("mysql".equals(dbType) || "mariadb".equals(dbType)) {
      mysql(packageName, dbUrl, dbId, dbPwd, currentProjectPath, entityFilePath, daoFilePath, sqlFilePath,
          serviceFilePath, parameterFilePath);
    } else if ("sqlserver".equals(dbType)) {
      mssql(packageName, dbUrl, dbId, dbPwd, currentProjectPath, entityFilePath, daoFilePath, sqlFilePath);
    } else if ("tibero".equals(dbType)) {
      tibero(packageName, dbUrl, dbId, dbPwd, currentProjectPath, entityFilePath, daoFilePath, sqlFilePath);
    } else if ("postgresql".equals(dbType)) {
      postgresql(packageName, dbUrl, dbId, dbPwd, currentProjectPath, entityFilePath, daoFilePath, sqlFilePath,
          serviceFilePath, parameterFilePath);
    } else {
      logger.info("{} 지원하지 않는 jdbc url 입니다. ", dbUrl);
    }

  }

  private static void mysql(String packageName, String dbUrl, String dbId, String dbPwd, String currentProjectPath,
      String entityFilePath, String daoFilePath, String sqlFilePath, String serviceFilePath, String parameterFilePath)
      throws IOException {
    // TableList 조회
    List<DbColumn> tableList = Mysql.getTableList(dbUrl, dbId, dbPwd);
    logger.info("---------------------Create mysql mybatis-------------------------");
    String content = GenerateSql.getMybatisMapperConfig(packageName + ".entity", "sql", tableList);
    GenerateConfig.generateConfigFile(packageName, content);
    for (DbColumn table : tableList) {
      if (!Constants.filter(table.getTableName())) {
        continue;
      }
      String tableName = table.getTableName();
      logger.info("{} 테이블 관련 GenerateEntity, GenerateDao, Sql 생성 ", tableName);

      List<DbColumn> columns = Mysql.getColumns(tableName, dbUrl, dbId, dbPwd);
      String tableComment = Mysql.getTableComment(tableName, dbUrl, dbId, dbPwd);
      GenerateParameter.create(parameterFilePath, tableName, columns);
      GenerateEntity.create(entityFilePath, tableName, columns, tableComment);
      GenerateDao.create(daoFilePath, tableName, columns);
      GenerateService.create(serviceFilePath, tableName, columns);

      GenerateSql.create(sqlFilePath, packageName + ".dao", tableName, columns, tableComment);
    }
  }
  private static void postgresql(String packageName, String dbUrl, String dbId, String dbPwd, String currentProjectPath,
      String entityFilePath, String daoFilePath, String sqlFilePath, String serviceFilePath, String parameterFilePath)
          throws IOException {
    // TableList 조회
    List<DbColumn> tableList = Postgre.getTableList(dbUrl, dbId, dbPwd);
    logger.info("---------------------Create Postgre mybatis-------------------------");
    String content = GenerateSql.getMybatisMapperConfig(packageName + ".entity", "sql", tableList);
    GenerateConfig.generateConfigFile(packageName, content);
    for (DbColumn table : tableList) {
      if (!Constants.filter(table.getTableName())) {
        continue;
      }
      String tableName = table.getTableName();
      logger.info("{} 테이블 관련 GenerateEntity, GenerateDao, Sql 생성 ", tableName);

      List<DbColumn> columns = Postgre.getColumns(tableName, dbUrl, dbId, dbPwd);
      String tableComment = Postgre.getTableComment(tableName, dbUrl, dbId, dbPwd);
      GenerateParameter.create(parameterFilePath, tableName, columns);
      GenerateEntity.create(entityFilePath, tableName, columns, tableComment);
      GenerateDao.create(daoFilePath, tableName, columns);
      GenerateService.create(serviceFilePath, tableName, columns);

      GenerateSql.create(sqlFilePath, packageName + ".dao", tableName, columns, tableComment);
    }
  }

  private static void mssql(String packageName, String dbUrl, String dbId, String dbPwd, String currentProjectPath,
      String entityFilePath, String daoFilePath, String sqlFilePath) throws IOException {
    // TableList 조회
    List<DbColumn> tableList = Mssql.getTableList(dbUrl, dbId, dbPwd);
    logger.info("---------------------Create mssql mybatis-------------------------");
    String content = GenerateSql.getMybatisMapperConfig(packageName + ".entity", "sql", tableList);

    GenerateConfig.generateConfigFile(packageName, content);
    for (DbColumn table : tableList) {
      if (!Constants.filter(table.getTableName())) {
        continue;
      }
      String tableName = table.getTableName();
      logger.info("{} 테이블 관련 GenerateEntity, GenerateDao, Sql 생성 ", tableName);

      List<DbColumn> columns = Mssql.getColumns(tableName, dbUrl, dbId, dbPwd);
      String tableComment = Mssql.getTableComment(tableName, dbUrl, dbId, dbPwd);
      GenerateEntity.create(entityFilePath, tableName, columns, tableComment);
      GenerateDao.create(daoFilePath, tableName, columns);

      GenerateSql.create(sqlFilePath, packageName + ".dao", tableName, columns, tableComment);
    }
  }

  private static void oracle(String packageName, String dbUrl, String dbId, String dbPwd, String currentProjectPath,
      String entityFilePath, String daoFilePath, String sqlFilePath) throws IOException, UnsupportedEncodingException {
    // TableList 조회
    List<DbColumn> tableList = Oracle.getTableList(dbUrl, dbId, dbPwd);
    logger.info("---------------------Create mybatis-------------------------");
    String content = GenerateSql.getMybatisMapperConfig(packageName + ".entity", "sql", tableList);

    GenerateConfig.generateConfigFile(packageName, content);
    for (DbColumn table : tableList) {
      if (!Constants.filter(table.getTableName())) {
        continue;
      }
      String tableName = table.getTableName();
      String tableComments = table.getTableComments();
      logger.info("{} 테이블 관련 GenerateEntity, GenerateDao, Sql 생성 ", tableName);

      List<DbColumn> columns = Oracle.getColumns(tableName, dbUrl, dbId, dbPwd);
      GenerateEntity.create(entityFilePath, tableName, columns, tableComments);
      GenerateDao.create(daoFilePath, tableName, columns);
      GenerateSql.create(sqlFilePath, packageName + ".dao", tableName, columns, tableComments);
    }
  }

  private static void tibero(String packageName, String dbUrl, String dbId, String dbPwd, String currentProjectPath,
      String entityFilePath, String daoFilePath, String sqlFilePath) throws IOException, UnsupportedEncodingException {
    // TableList 조회
    List<DbColumn> tableList = Tibero.getTableList(dbUrl, dbId, dbPwd);
    logger.info("---------------------Create mybatis-------------------------");
    String content = GenerateSql.getMybatisMapperConfig(packageName + ".entity", "sql", tableList);

    GenerateConfig.generateConfigFile(packageName, content);
    for (DbColumn table : tableList) {
      if (!Constants.filter(table.getTableName())) {
        continue;
      }
      String tableName = table.getTableName();
      String tableComments = table.getTableComments();
      logger.info("{} 테이블 관련 GenerateEntity, GenerateDao, Sql 생성 ", tableName);

      List<DbColumn> columns = Tibero.getColumns(tableName, dbUrl, dbId, dbPwd);
      GenerateEntity.create(entityFilePath, tableName, columns, tableComments);
      GenerateDao.create(daoFilePath, tableName, columns);
      GenerateSql.create(sqlFilePath, packageName + ".dao", tableName, columns, tableComments);
    }
  }

  private static void cubrid(String packageName, String dbUrl, String dbId, String dbPwd, String currentProjectPath,
      String entityFilePath, String daoFilePath, String sqlFilePath) throws IOException, UnsupportedEncodingException {

    // TableList 조회
    List<DbColumn> tableList = Cubrid.getTableList(dbUrl, dbId, dbPwd);
    logger.info("---------------------Create mybatis-------------------------");
    String content = GenerateSql.getMybatisMapperConfig(packageName + ".entity", "sql", tableList);

    GenerateConfig.generateConfigFile(packageName, content);
    for (DbColumn table : tableList) {
      if (!Constants.filter(table.getTableName())) {
        continue;
      }
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
