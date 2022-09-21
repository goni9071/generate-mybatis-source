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

public class GenerateService {
  private static Logger logger = LoggerFactory.getLogger(GenerateService.class);
  public static final String SUFFIX_SERVICE_CLASS_NAME = "Service";

  private static final String REPLACE_PACKAGE_NAME = "%packageName%";
  private static final String REPLACE_SERVICE_CLASS_NAME = "%className%";
  private static final String REPLACE_ENTITY_CLASS_NAME = "%entityName%";
  private static final String REPLACE_PK_DATA_TYPE = "%pkDataType%";

  public static void create(String filePath, String tableName, List<DbColumn> filedList) throws UnsupportedEncodingException, IOException {
    String serviceClassName = StringUtil.convertCamelNaming(tableName) + SUFFIX_SERVICE_CLASS_NAME;
    String entityClassName = StringUtil.convertCamelNaming(tableName) + GenerateEntity.SUFFIX_ENTITY_CLASS_NAME;
    String serviceTemplate = TemplateUtil.getServiceTemplate();

    String pkDataType = "String";
    for (DbColumn column : filedList) {
      String dataType = column.getDataType();
      if (GenerateSql.PRIMARY_KEY.contains("," + column.getConstrainst() + ",")) {
        pkDataType = StringUtil.convertDataType(dataType);
        break;
      }
    }
    serviceTemplate = serviceTemplate.replaceAll(REPLACE_PACKAGE_NAME, Constants.PACKAGE_BASE);
    serviceTemplate = serviceTemplate.replaceAll(REPLACE_SERVICE_CLASS_NAME, serviceClassName);
    serviceTemplate = serviceTemplate.replaceAll(REPLACE_ENTITY_CLASS_NAME, entityClassName);
    serviceTemplate = serviceTemplate.replaceAll(REPLACE_PK_DATA_TYPE, pkDataType);

    if (!FileUtil.existDirectory(filePath)) {
      FileUtil.makeDirectory(filePath);
    }
    FileUtil.fileWrite(filePath + "/" + serviceClassName + ".java", serviceTemplate);

    if (FileUtil.existFile(filePath + "/" + serviceClassName + ".java") && logger.isDebugEnabled()) {
      logger.debug("GenerateService 파일 생성 : {} ", serviceClassName + ".java");
    }
  }

  public static void createServiceStaticTemplate(String filePath, String packageName) {
    String[] templates = { "BaseService.java" };
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
          GenerateConfig.logger.info("GenerateServcie 관련 파일 생성 : {} ", template);
        }
      } catch (IOException e) {
        GenerateConfig.logger.error(template + " 생성오류", e);
      }
    }
  }
}
