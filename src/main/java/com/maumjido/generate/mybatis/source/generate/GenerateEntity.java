package com.maumjido.generate.mybatis.source.generate;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maumjido.generate.mybatis.source.common.Constants;
import com.maumjido.generate.mybatis.source.db.DbColumn;
import com.maumjido.generate.mybatis.source.util.FileUtil;
import com.maumjido.generate.mybatis.source.util.StringUtil;

public class GenerateEntity {
  private static Logger logger = LoggerFactory.getLogger(GenerateEntity.class);

  private static final String PACKAGE_NAME = Constants.PACKAGE_BASE + ".entity";

  public static void create(String filePath, String tableName, List<DbColumn> filedList, String tableComment)
      throws IOException {
    String className = StringUtil.convertCamelNaming(tableName);
    StringBuffer sb = new StringBuffer();
    sb.append("package ").append(PACKAGE_NAME).append(";\r\n");

    sb.append("import ").append(PACKAGE_NAME).append(".base.").append(className + "Base").append(";\r\n");
    sb.append("import lombok.Getter;\r\n");
    sb.append("import lombok.Setter;\r\n");
    sb.append("import lombok.AllArgsConstructor;\r\n");
    sb.append("import lombok.NoArgsConstructor;\r\n");
    sb.append("import lombok.experimental.SuperBuilder;\r\n");

    sb.append("/**").append("\r\n");
    sb.append(" * ").append(tableComment).append(" ").append(" Class").append("\r\n");
    sb.append(" */").append("\r\n");
    sb.append("@Getter\r\n");
    sb.append("@Setter\r\n");
    sb.append("@AllArgsConstructor\r\n");
    sb.append("@NoArgsConstructor\r\n");
    sb.append("@SuperBuilder\r\n");
    sb.append("public class ").append(className).append(" extends ").append(className + "Base").append(" {\r\n");
    sb.append("}");
    // logger.info(sb.toString());

    if (!FileUtil.existDirectory(filePath)) {
      FileUtil.makeDirectory(filePath);
    }

    if (!FileUtil.existDirectory(filePath)) {
      FileUtil.makeDirectory(filePath);
    }
    String contents = sb.toString();

    FileUtil.fileWrite(filePath + "/" + className + ".java", contents);

    if (FileUtil.existFile(filePath + "/" + className + ".java") && logger.isDebugEnabled()) {
      logger.debug("GenerateEntity 파일 생성 : {}", className + ".java");
    }

    createBase(filePath + "/base", tableName, filedList, tableComment);
  }

  public static void createBase(String filePath, String tableName, List<DbColumn> filedList, String tableComment)
      throws IOException {
    StringBuffer sb = new StringBuffer();
    StringBuffer gettersetter = new StringBuffer();
    sb.append("package ").append(PACKAGE_NAME).append(".base;\r\n");
    sb.append("[IMPORT DATE]");
    sb.append("import lombok.Getter;\r\n");
    sb.append("import lombok.Setter;\r\n");
    sb.append("import lombok.AllArgsConstructor;\r\n");
    sb.append("import lombok.NoArgsConstructor;\r\n");
    sb.append("import lombok.experimental.SuperBuilder;\r\n");

    sb.append("/**").append("\r\n");
    sb.append(" * ").append(tableComment).append(" ").append(" Class").append("\r\n");
    sb.append(" */").append("\r\n");

    sb.append("@Getter\r\n");
    sb.append("@Setter\r\n");
    sb.append("@AllArgsConstructor\r\n");
    sb.append("@NoArgsConstructor\r\n");
    sb.append("@SuperBuilder\r\n");

    sb.append("public class ").append(StringUtil.convertCamelNaming(tableName) + "Base");
    sb.append(" {\r\n");
    boolean hasDateType = false;
    for (DbColumn column : filedList) {
      String dataType = column.getDataType();
      dataType = StringUtil.convertDataType(dataType);
      String field1 = StringUtil.convertCamelNaming(column.getColumnName(), false);
      String Comment = StringUtil.convertCamelNaming(column.getComments(), false);
      if (!"".equals(Comment)) {
        sb.append("    private ").append(dataType).append(" ").append(field1).append(";//").append(Comment)
            .append("\r\n");
      } else {
        sb.append("    private ").append(dataType).append(" ").append(field1).append(";\r\n");
      }

      if ("Date".equals(dataType)) {
        hasDateType = true;
      }
    }

    sb.append(gettersetter.toString());
    sb.append("}");
    // logger.info(sb.toString());

    if (!FileUtil.existDirectory(filePath)) {
      FileUtil.makeDirectory(filePath);
    }

    if (!FileUtil.existDirectory(filePath)) {
      FileUtil.makeDirectory(filePath);
    }
    String contents = sb.toString();

    if (hasDateType) {
      contents = contents.replace("[IMPORT DATE]", "import java.util.Date;\r\n");
    } else {
      contents = contents.replace("[IMPORT DATE]", "");
    }
    FileUtil.fileWrite(filePath + "/" + StringUtil.convertCamelNaming(tableName) + "Base.java", contents);

    if (FileUtil.existFile(filePath + "/" + StringUtil.convertCamelNaming(tableName) + "Base.java")
        && logger.isDebugEnabled()) {
      logger.debug("GenerateEntity 파일 생성 : {}", StringUtil.convertCamelNaming(tableName) + "Base.java");
    }

  }
}
