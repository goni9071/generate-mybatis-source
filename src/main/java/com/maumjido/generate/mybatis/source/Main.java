package com.maumjido.generate.mybatis.source;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maumjido.generate.mybatis.source.common.Constants;
import com.maumjido.generate.mybatis.source.generate.MybatisGenerate;
import com.maumjido.generate.mybatis.source.util.FileUtil;

public class Main {

  private static Logger logger = LoggerFactory.getLogger(Main.class);

  /**
   * Generate-java-resource 생성
   * 
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws IOException {

    logger.info("----------------------------------------------");
    logger.info("Generate-Java-Resource Start");

    File projectPath = new File(".");
    String currentProjectPath = projectPath.getAbsoluteFile().toString().replaceAll("\\.", "");
    logger.info("현재 프로젝트 경로 {} : ", currentProjectPath);

    FileUtil.removeDir(currentProjectPath + "/result/src");

    MybatisGenerate.generate(Constants.PACKAGE_BASE, Constants.DB_URL, Constants.DB_ID, Constants.DB_PWD, currentProjectPath);

    logger.info("----------------------------------------------");
    logger.info("Generate-Java-Resource End");
    logger.info("----------------------------------------------");
  }

}
