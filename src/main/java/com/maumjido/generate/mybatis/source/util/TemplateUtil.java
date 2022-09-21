package com.maumjido.generate.mybatis.source.util;

import java.io.IOException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TemplateUtil {

  private static Logger logger = LoggerFactory.getLogger(TemplateUtil.class);

  public static String getDaoTemplate() {
    // dao template 읽기
    return TemplateUtil.getTemplate("DaoTemplate.java");
  }

  public static String getServiceTemplate() {
    // dao template 읽기
    return TemplateUtil.getTemplate("ServiceTemplate.java");
  }
  public static String getParameterTemplate() {
    // dao template 읽기
    return TemplateUtil.getTemplate("ParameterTemplate.java");
  }

  public static String getTemplate(String templatePath) {
    logger.info("--------------------------------------------------------");
    logger.info("템플릿 파일 경로 : {}", "template/" + templatePath);
    String result = "";
    // 템플릿 파일 읽기
    URL fileUrl = ClassLoader.getSystemResource("template/" + templatePath);
    if (fileUrl != null) {
      try {
        result = FileUtil.readFileByLineForString(fileUrl.getFile());
      } catch (IOException e) {
        throw new RuntimeException("템플리 읽기 실패:" + templatePath, e);
      }
    } else {
      throw new RuntimeException(templatePath + " not found");
    }
    return result;
  }
}
