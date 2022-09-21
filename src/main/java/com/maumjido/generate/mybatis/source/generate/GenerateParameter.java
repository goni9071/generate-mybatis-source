package com.maumjido.generate.mybatis.source.generate;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maumjido.generate.mybatis.source.common.Constants;
import com.maumjido.generate.mybatis.source.db.DbColumn;
import com.maumjido.generate.mybatis.source.util.FileUtil;
import com.maumjido.generate.mybatis.source.util.StringUtil;
import com.maumjido.generate.mybatis.source.util.TemplateUtil;

public class GenerateParameter {
  private static Logger logger = LoggerFactory.getLogger(GenerateParameter.class);
  public static final String SUFFIX_PARAM_CLASS_NAME = "Param";

  private static final String REPLACE_PACKAGE_NAME = "%packageName%";
  private static final String REPLACE_PARAM_CLASS_NAME = "%className%";
  private static final String REPLACE_ENTITY_CLASS_NAME = "%entityName%";
  private static final String REPLACE_CLASS_CONTENTS = "%classContents%";

  public static void create(String filePath, String tableName, List<DbColumn> filedList)
      throws UnsupportedEncodingException, IOException {
    String paramClassName = StringUtil.convertCamelNaming(tableName) + SUFFIX_PARAM_CLASS_NAME;
    String entityClassName = StringUtil.convertCamelNaming(tableName) + GenerateEntity.SUFFIX_ENTITY_CLASS_NAME;
    String parameterTemplate = TemplateUtil.getParameterTemplate();

    parameterTemplate = parameterTemplate.replaceAll(REPLACE_PACKAGE_NAME, Constants.PACKAGE_BASE);
    parameterTemplate = parameterTemplate.replaceAll(REPLACE_PARAM_CLASS_NAME, paramClassName);
    parameterTemplate = parameterTemplate.replaceAll(REPLACE_ENTITY_CLASS_NAME, entityClassName);

    boolean hasDateType = false;
    StringBuffer sb = new StringBuffer();
    for (DbColumn column : filedList) {
      String dataType = column.getDataType();
      dataType = StringUtil.convertDataType(dataType);
      String field1 = StringUtil.convertCamelNaming(column.getColumnName(), false);
      String Comment = StringUtil.convertCamelNaming(column.getComments(), false);
      if (!"".equals(Comment)) {
        sb.append("  private ").append(dataType).append(" ").append(field1).append(";//").append(Comment)
            .append("\r\n");
      } else {
        sb.append("  private ").append(dataType).append(" ").append(field1).append(";\r\n");
      }

      if ("Date".equals(dataType)) {
        hasDateType = true;
      }
    }

    parameterTemplate = parameterTemplate.replaceAll(REPLACE_CLASS_CONTENTS, sb.toString());
    if (hasDateType) {
      parameterTemplate = parameterTemplate.replace("[IMPORT]", "import java.util.Date;\r\n");
    } else {
      parameterTemplate = parameterTemplate.replace("[IMPORT]", "");
    }

    if (!FileUtil.existDirectory(filePath)) {
      FileUtil.makeDirectory(filePath);
    }
    FileUtil.fileWrite(filePath + "/" + paramClassName + ".java", parameterTemplate);

    if (FileUtil.existFile(filePath + "/" + paramClassName + ".java") && logger.isDebugEnabled()) {
      logger.debug("GenerateParameter 파일 생성 : {} ", paramClassName + ".java");
    }
  }

  public static void createParameterStaticTemplate(String filePath, String packageName) {
    String[] templates = { "SearchParameter.java" };
    for (String template : templates) {
      String result = null;
      try {
        result = TemplateUtil.getTemplate(template);
        result = result.replaceAll("%packageName%", packageName);

        if (!FileUtil.existDirectory(filePath)) {
          FileUtil.makeDirectory(filePath);
        }
        FileUtil.fileWrite(filePath + File.separator + template, result);

        if (FileUtil.existFile(filePath + File.separator + template)) {
          GenerateConfig.logger.info("GenerateParameter 관련 파일 생성 : {} ", template);
        }
      } catch (IOException e) {
        GenerateConfig.logger.error(template + " 생성오류", e);
      }
    }
  }
}
