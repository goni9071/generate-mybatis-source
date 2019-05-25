package com.maumjido.generate.mybatis.source;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maumjido.generate.mybatis.source.common.Constants;
import com.maumjido.generate.mybatis.source.generate.GenerateController;
import com.maumjido.generate.mybatis.source.generate.GenerateJsp;
import com.maumjido.generate.mybatis.source.generate.GenerateUrl;
import com.maumjido.generate.mybatis.source.util.FileUtil;
import com.maumjido.generate.mybatis.source.util.JsonUtil;
import com.maumjido.generate.mybatis.source.util.MenuUtil;
import com.maumjido.generate.mybatis.source.util.MenuUtil.Menu;

public class MenuGenerator {

  private static Logger logger = LoggerFactory.getLogger(MenuGenerator.class);

  public static void main(String[] args) throws IOException, InvalidFormatException {

    logger.info("----------------------------------------------");
    logger.info("Generate Menu Start");

    File projectPath = new File(".");
    String currentProjectPath = projectPath.getAbsoluteFile().toString().replaceAll("\\.", "");
    logger.info("현재 프로젝트 경로 {} : ", currentProjectPath);

    FileUtil.removeDir(currentProjectPath + "/result/src");

    List<Menu> menuList = MenuUtil.getMenuList();
    logger.info(JsonUtil.toJson(menuList));
    GenerateController.create(menuList);
    GenerateJsp.create(menuList);
    
    if(!"Y".equals(Constants.OPTION_USE_URL_CODE)) {
      GenerateUrl.create(menuList);
    }
    logger.info("----------------------------------------------");
    logger.info("Generate Menu End");
    logger.info("----------------------------------------------");
  }

}
