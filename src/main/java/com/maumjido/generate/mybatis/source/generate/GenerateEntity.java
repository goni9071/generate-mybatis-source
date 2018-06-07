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
  public static final String SUFFIX_ENTITY_CLASS_NAME = "";

  private static final String PARENT_ENTITY = null;
  private static final String PACKAGE_NAME = Constants.PACKAGE_BASE + ".entity";

  public static void create(String filePath, String tableName, List<DbColumn> filedList, String tableComment) throws IOException {
    StringBuffer sb = new StringBuffer();
    StringBuffer gettersetter = new StringBuffer();
    sb.append("package ").append(PACKAGE_NAME).append(";\r\n");
    if (PARENT_ENTITY != null) {
      sb.append("import ").append(PACKAGE_NAME).append(".base.").append(PARENT_ENTITY).append(";\r\n");
    }
    sb.append("[IMPORT DATE]");
    sb.append("/**").append("\r\n");
    sb.append(" * ").append(tableComment).append(" ").append(SUFFIX_ENTITY_CLASS_NAME).append(" Class").append("\r\n");
    sb.append(" */").append("\r\n");
    sb.append("public class ").append(StringUtil.convertCamelNaming(tableName)).append(SUFFIX_ENTITY_CLASS_NAME);
    if (PARENT_ENTITY != null) {
      sb.append(" extends ").append(PARENT_ENTITY);
    }
    sb.append(" {\r\n");
    boolean hasDateType = false;
    for (DbColumn column : filedList) {
      String dataType = column.getDataType();
      dataType = StringUtil.convertDataType(dataType);
      String field1 = StringUtil.convertCamelNaming(column.getColumnName(), false);
      String Comment = StringUtil.convertCamelNaming(column.getComments(), false);
      String field2 = StringUtil.convertCamelNaming(column.getColumnName());
      if (!"".equals(Comment)) {
        sb.append("    private ").append(dataType).append(" ").append(field1).append(";//").append(Comment).append("\r\n");
      } else {
        sb.append("    private ").append(dataType).append(" ").append(field1).append(";\r\n");
      }

      gettersetter.append("   /**").append("\r\n");
      gettersetter.append("    * ").append(Comment + " 조회").append("\r\n");
      gettersetter.append("    * ").append("@return").append(" ").append(field1).append("\r\n");
      gettersetter.append("    */").append("\r\n");
      gettersetter.append("    public ").append(dataType).append(" get").append(field2).append("() {\r\n");
      gettersetter.append("        return this.").append(field1).append(";\r\n");
      gettersetter.append("    }\r\n");
      gettersetter.append("   /**").append("\r\n");
      gettersetter.append("    * ").append(Comment + " 설정").append("\r\n");
      gettersetter.append("    * ").append("@return").append(" ").append(field1).append("\r\n");
      gettersetter.append("    */").append("\r\n");
      gettersetter.append("    public void set").append(field2).append("(").append(dataType).append(" ").append(field1).append(") {\r\n");
      gettersetter.append("        this.").append(field1).append(" = ").append(field1).append(";\r\n");
      gettersetter.append("    }\r\n");
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
    if (PARENT_ENTITY != null) {
      contents = contents.replace(PARENT_ENTITY, "BaseModel");
    }

    if (hasDateType) {
      contents = contents.replace("[IMPORT DATE]", "import java.util.Date;\r\n");
    } else {
      contents = contents.replace("[IMPORT DATE]", "");
    }
    FileUtil.fileWrite(filePath + "/" + StringUtil.convertCamelNaming(tableName) + SUFFIX_ENTITY_CLASS_NAME + ".java", contents);

    if (FileUtil.existFile(filePath + "/" + StringUtil.convertCamelNaming(tableName) + SUFFIX_ENTITY_CLASS_NAME + ".java") && logger.isDebugEnabled()) {
      logger.debug("GenerateEntity 파일 생성 : {}", StringUtil.convertCamelNaming(tableName) + SUFFIX_ENTITY_CLASS_NAME + ".java");
    }

  }
}
