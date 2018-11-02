package com.maumjido.generate.mybatis.source.generate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maumjido.generate.mybatis.source.common.Constants;
import com.maumjido.generate.mybatis.source.db.DbColumn;
import com.maumjido.generate.mybatis.source.util.FileUtil;
import com.maumjido.generate.mybatis.source.util.StringUtil;

public class GenerateSql {
  public static Logger logger = LoggerFactory.getLogger(GenerateSql.class);
  public static final String PRIMARY_KEY = ",PK,PRI,";

  public static void create(String filePath, String daoPackageName, String tableName, List<DbColumn> filedList, String tableComment) throws UnsupportedEncodingException, IOException {
    StringBuffer sql = new StringBuffer();
    StringBuffer result = new StringBuffer();
    String tableNameUpper = StringUtil.convertCamelNaming(tableName);
    String tableNameLower = StringUtil.convertCamelNaming(tableName, false);
    sql.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n");
    sql.append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\r\n");
    sql.append("<mapper namespace=\"").append(daoPackageName).append(".").append(tableNameUpper).append(GenerateDao.SUFFIX_DAO_CLASS_NAME).append("\">\r\n");
    sql.append("    <resultMap type=\"").append(tableNameUpper).append("\" id=\"").append(tableNameUpper).append("RM\">\r\n");
    for (DbColumn column : filedList) {
      String dataType = column.getDataType();
      String javaType = StringUtil.convertDataType(dataType);
      String columnName = column.getColumnName();
      String comment = column.getComments();
      String property = StringUtil.convertCamelNaming(columnName, false);
      if (GenerateSql.PRIMARY_KEY.contains("," + column.getConstrainst() + ",")) {
        sql.append("        <id property=\"").append(property).append("\" column=\"").append(columnName).append("\" javaType=\"").append(javaType).append("\"></id>").append("<!--").append(comment)
            .append("-->\r\n");
      } else {
        result.append("        <result property=\"").append(property).append("\" column=\"").append(columnName).append("\" javaType=\"").append(javaType).append("\"></result>").append("<!--")
            .append(comment).append("-->\r\n");
      }
    }
    sql.append(result);
    sql.append("    </resultMap>\r\n");
    sql.append(GenerateSql.getInsertQuery(tableName, filedList, tableComment));
    sql.append(GenerateSql.getUpdateQuery(tableName, filedList, tableComment));
    sql.append(GenerateSql.getDeleteQuery(tableName, filedList, tableComment));
    sql.append(GenerateSql.getSelectQuery(tableName, filedList, tableComment));
    sql.append(GenerateSql.getSelectListQuery(tableName, filedList, tableComment));
    sql.append(GenerateSql.getSelectListCntQuery(tableName, filedList, tableComment));
    sql.append("</mapper>");

    String fileFullPath = filePath + "/" + tableNameLower + ".xml";

    if (!FileUtil.existDirectory(filePath)) {
      FileUtil.makeDirectory(filePath);
    }
    FileUtil.fileWrite(fileFullPath, sql.toString());

    if (FileUtil.existFile(fileFullPath) && GenerateSql.logger.isDebugEnabled()) {
      GenerateSql.logger.debug("sql 파일 생성 : {}", tableNameLower + ".xml");
    }

  }

  public static String getInsertQuery(String tableName, List<DbColumn> filedList, String tableComment) {
    StringBuffer insertQuery = new StringBuffer();
    StringBuffer values = new StringBuffer("             ) VALUES (\r\n");
    String tableNameUpper = StringUtil.convertCamelNaming(tableName);
    insertQuery.append("    <insert id=\"insert").append("").append("\" parameterType=\"").append(tableNameUpper).append("\">\r\n");
    insertQuery.append("        /* ").append(tableComment).append(" 등록").append(" */").append("\r\n");
    insertQuery.append("        INSERT INTO ").append(tableName).append(" (\r\n");
    String priKey = null;
    String priType = null;
    for (int i = 0; i < filedList.size(); i++) {
      DbColumn column = filedList.get(i);
      String dataType = column.getDataType();
      String javaType = StringUtil.convertDataType(dataType);
      String columnName = column.getColumnName();
      String property = StringUtil.convertCamelNaming(columnName, false);
      if ("auto_increment".equals(column.getExtra())) {
        priKey = property;
        priType = javaType;
        continue;
      }

      if (i + 1 == filedList.size()) {
        insertQuery.append("               ").append(columnName.toLowerCase()).append("\r\n");
        values.append("               #{entity.").append(property).append("})\r\n");
      } else {
        insertQuery.append("               ").append(columnName.toLowerCase()).append(",\r\n");
        values.append("               #{entity.").append(property).append("},\r\n");
      }
    }
    insertQuery.append(values);
    if (priKey != null) {
      insertQuery.append("        <selectKey resultType=\"").append(priType).append("\" keyProperty=\"entity.").append(priKey).append("\">\r\n");
      insertQuery.append("            SELECT LAST_INSERT_ID()\r\n");
      insertQuery.append("        </selectKey>\r\n");
    }

    insertQuery.append("    </insert>\r\n");
    return insertQuery.toString();
  }

  public static String getUpdateQuery(String tableName, List<DbColumn> filedList, String tableComment) {
    StringBuffer updateQuery = new StringBuffer();
    StringBuffer where = new StringBuffer("         WHERE ");
    String tableNameUpper = StringUtil.convertCamelNaming(tableName);
    updateQuery.append("    <update id=\"update").append("").append("\" parameterType=\"").append(tableNameUpper).append("\">\r\n");
    updateQuery.append("        /* ").append(tableComment).append(" 수정").append(" */").append("\r\n");
    updateQuery.append("        UPDATE ").append(tableName).append(" \r\n");
    updateQuery.append("               <set>\r\n");
    int whereCnt = 0;
    for (int i = 0; i < filedList.size(); i++) {
      DbColumn column = filedList.get(i);
      String columnName = column.getColumnName();
      String property = StringUtil.convertCamelNaming(columnName, false);
      String lowerColumnName = columnName.toLowerCase();
      if (PRIMARY_KEY.contains("," + column.getConstrainst() + ",")) {
        if (whereCnt == 0) {
          where.append(lowerColumnName).append(" = #{entity.").append(property).append("}\r\n");
          whereCnt++;
        } else {
          where.append("           AND ").append(lowerColumnName).append(" = #{entity.").append(property).append("}\r\n");
        }
        continue;
      }

      updateQuery.append("               <if test=\"entity.").append(property).append(" != null\">\r\n");
      updateQuery.append("               ").append(lowerColumnName).append(" = #{entity.").append(property).append("},\r\n");
      updateQuery.append("               </if>\r\n");
    }
    updateQuery.append("               </set>\r\n");
    updateQuery.append(where);
    updateQuery.append("    </update>\r\n");
    return updateQuery.toString();
  }

  public static String getDeleteQuery(String tableName, List<DbColumn> filedList, String tableComment) {
    StringBuffer deleteQuery = new StringBuffer();
    StringBuffer where = new StringBuffer("         WHERE ");
    String tableNameUpper = StringUtil.convertCamelNaming(tableName);
    deleteQuery.append("    <delete id=\"delete").append("").append("\" parameterType=\"").append(tableNameUpper).append("\">\r\n");
    deleteQuery.append("        /* ").append(tableComment).append(" 삭제").append(" */").append("\r\n");
    deleteQuery.append("        DELETE FROM ").append(tableName).append(" \r\n");
    int whereCnt = 0;
    for (int i = 0; i < filedList.size(); i++) {
      DbColumn column = filedList.get(i);
      String columnName = column.getColumnName();
      String property = StringUtil.convertCamelNaming(columnName, false);
      if (PRIMARY_KEY.contains("," + column.getConstrainst() + ",")) {
        if (whereCnt == 0) {
          where.append(columnName.toLowerCase()).append(" = #{").append(property).append("}\r\n");
          whereCnt++;
        } else {
          where.append("           AND ").append(columnName.toLowerCase()).append(" = #{").append(property).append("}\r\n");
        }
        continue;
      }
    }
    deleteQuery.append(where);
    deleteQuery.append("    </delete>\r\n");
    return deleteQuery.toString();
  }

  public static String getSelectQuery(String tableName, List<DbColumn> filedList, String tableComment) {
    StringBuffer selectQuery = new StringBuffer();
    StringBuffer where = new StringBuffer("         WHERE ");
    String tableNameUpper = StringUtil.convertCamelNaming(tableName);
    selectQuery.append("    <select id=\"selectOne").append("").append("\" resultMap=\"").append(tableNameUpper).append("RM\">\r\n");
    selectQuery.append("        /* ").append(tableComment).append(" 상세조회").append(" */").append("\r\n");
    selectQuery.append("        SELECT ");
    int whereCnt = 0;
    boolean isFirst = true;
    for (int i = 0; i < filedList.size(); i++) {
      DbColumn column = filedList.get(i);
      String columnName = column.getColumnName();
      String property = StringUtil.convertCamelNaming(columnName, false);
      if (PRIMARY_KEY.contains("," + column.getConstrainst() + ",")) {
        if (whereCnt == 0) {
          where.append(columnName.toLowerCase()).append(" = #{").append(property).append("}\r\n");
          whereCnt++;
        } else {
          where.append("           AND ").append(columnName.toLowerCase()).append(" = #{").append(property).append("}\r\n");
        }
      }

      if (i + 1 == filedList.size()) {
        if (isFirst) {
          selectQuery.append(columnName.toLowerCase()).append("\r\n");
          isFirst = false;
        } else {
          selectQuery.append("               ").append(columnName.toLowerCase()).append("\r\n");
        }
      } else {
        if (isFirst) {
          selectQuery.append(columnName.toLowerCase()).append(",\r\n");
          isFirst = false;
        } else {
          selectQuery.append("               ").append(columnName.toLowerCase()).append(",\r\n");
        }
      }
    }
    selectQuery.append("          FROM ").append(tableName).append("\r\n");
    selectQuery.append(where);
    selectQuery.append("    </select>\r\n");
    return selectQuery.toString();
  }

  public static String getSelectListQuery(String tableName, List<DbColumn> filedList, String tableComment) {
    String dbType = MybatisGenerate.getDbType(Constants.DB_URL);
    StringBuffer selectQuery = new StringBuffer();
    StringBuffer where = new StringBuffer();
    String tableNameUpper = StringUtil.convertCamelNaming(tableName);
    selectQuery.append("    <select id=\"selectList").append("").append("\" resultMap=\"").append(tableNameUpper).append("RM\">\r\n");
    selectQuery.append("        /* ").append(tableComment).append(" 목록조회").append(" */").append("\r\n");
    if ("oracle".equals(dbType)) {
      selectQuery.append("        SELECT * FROM (\r\n");
      selectQuery.append("        SELECT ");
    } else {
      selectQuery.append("        SELECT ");
    }
    // int whereCnt = 0;
    boolean isFirst = true;
    String primariKey = "여기여기";
    for (int i = 0; i < filedList.size(); i++) {
      DbColumn column = filedList.get(i);
      String columnName = column.getColumnName();
      String lowerCaseColumnName = columnName.toLowerCase();
      if (PRIMARY_KEY.contains("," + column.getConstrainst() + ",")) {
        primariKey = lowerCaseColumnName;
      }

      if (isFirst) {
        selectQuery.append(lowerCaseColumnName);
        isFirst = false;
      } else {
        selectQuery.append("               ").append(lowerCaseColumnName);
      }
      if (i < filedList.size() - 1) {
        selectQuery.append(",\r\n");
      } else {
        selectQuery.append("\r\n");
      }
    }
    if ("oracle".equals(dbType)) {
      selectQuery.append("               ROW_NUMBER() OVER(ORDER BY ").append(primariKey).append(" DESC NULLS LAST) ROWNUMBER\r\n");
    }
    selectQuery.append("          FROM ").append(tableName).append("\r\n");
    if ("oracle".equals(dbType)) {
      selectQuery.append("               ) ").append(tableName).append("\r\n");
    }
    where.append("               <if test=\"pageable != null\">\r\n");
    if ("oracle".equals(dbType)) {
      where.append("         WHERE ROWNUMBER BETWEEN #{pageable.start} AND #{pageable.end}\r\n");
    } else {
      where.append("         WHERE LIMIT #{pageable.start}, #{pageable.end}\r\n");
    }
    where.append("               </if>\r\n");
    selectQuery.append(where);
    selectQuery.append("    </select>\r\n");
    return selectQuery.toString();
  }

  public static String getSelectListCntQuery(String tableName, List<DbColumn> filedList, String tableComment) {
    StringBuffer selectQuery = new StringBuffer();
    StringBuffer where = new StringBuffer("         WHERE ");
    selectQuery.append("    <select id=\"selectListCount").append("").append("\" resultType=\"int\">\r\n");
    selectQuery.append("        /* ").append(tableComment).append(" 전체 개수 조회").append(" */").append("\r\n");
    selectQuery.append("        SELECT COUNT(*) AS CNT\r\n");
    int whereCnt = 0;
    for (int i = 0; i < filedList.size(); i++) {
      DbColumn column = filedList.get(i);
      String columnName = column.getColumnName();
      String property = StringUtil.convertCamelNaming(columnName, false);
      if (PRIMARY_KEY.contains("," + column.getConstrainst() + ",")) {
        if (whereCnt == 0) {
          where.append(columnName.toLowerCase()).append(" = #{").append(property).append("}\r\n");
          whereCnt++;
        } else {
          where.append("           AND ").append(columnName.toLowerCase()).append(" = #{").append(property).append("}\r\n");
        }
        continue;
      }
    }
    selectQuery.append("          FROM ").append(tableName).append("\r\n");
    selectQuery.append(where);
    selectQuery.append("    </select>\r\n");
    return selectQuery.toString();
  }

  public static String getMybatisMapperConfig(String packageName, String classpath, List<DbColumn> tableNameList) {
    StringBuffer mappers = new StringBuffer();
    mappers.append("    <mappers>\r\n");
    for (DbColumn table : tableNameList) {
      if (StringUtil.isEmpty(Constants.INCLUDE_PREFIX_TABLENAME) || table.getTableName().startsWith(Constants.INCLUDE_PREFIX_TABLENAME)) {
        String tableName = StringUtil.convertCamelNaming(table.getTableName(), false);
        mappers.append("        <mapper resource=\"").append(classpath).append("/").append(tableName).append(".xml\" />\r\n");
      }
    }
    mappers.append("    </mappers>\r\n");
    return mappers.toString();
  }

}
