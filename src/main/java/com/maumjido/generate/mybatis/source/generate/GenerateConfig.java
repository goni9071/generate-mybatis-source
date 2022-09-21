package com.maumjido.generate.mybatis.source.generate;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maumjido.generate.mybatis.source.util.FileUtil;
import com.maumjido.generate.mybatis.source.util.TemplateUtil;

public class GenerateConfig {

  static Logger logger = LoggerFactory.getLogger(GenerateConfig.class);

  public static void generateConfigFile(String packageName, String mapperConfig) {
    String targetPath = "result" + File.separator + "src" + File.separator + "main" + File.separator + "resources"
        + File.separator + "common" + File.separator + "config" + File.separator + "";

    try {
      if (!FileUtil.existDirectory(targetPath)) {
        FileUtil.makeDirectory(targetPath);
      }

      String result;
      result = TemplateUtil.getTemplate("mybatis-config.xml");
      result = result.replaceAll("%mybatisConfigContent%", mapperConfig);
      result = result.replaceAll("%packageName%", packageName);
      FileUtil.fileWrite(targetPath + "mybatis-config.xml", result);

      if (FileUtil.existFile("mybatis-config.xml", targetPath)) {
        logger.info("mybatis-config.xml 파일 생성");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

}
