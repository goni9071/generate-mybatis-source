package com.maumjido.generate.mybatis.source.generate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maumjido.generate.mybatis.source.util.FileUtil;
import com.maumjido.generate.mybatis.source.util.MenuUtil.Menu;
import com.maumjido.generate.mybatis.source.util.TemplateUtil;

public class GenerateJsp {
  private static Logger logger = LoggerFactory.getLogger(GenerateJsp.class);

  public static void create(List<Menu> menuList) {
    createJsp(getJspContent(menuList), "result/jsp", TemplateUtil.getTemplate("jspTemplate.jsp"));
  }

  public static Map<String, List<String>> getJspContent(List<Menu> menuList) {
    logger.info("--------------------------------------------------------");
    logger.info("jsp 파일 만들기 위한 데이터 가공 시작.");
    Map<String, List<String>> map = new HashMap<String, List<String>>();

    for (Menu menu : menuList) {
      String url = menu.getUrl();
      if (url.isEmpty()) {
        continue;
      }
      String jspFilePath = "null".equals(menu.getJsp()) ? "" : menu.getJsp();
      String className = "";

      if ("/".equals(url)) {
        className = "Main";
      } else {
        className = url.split("/")[1];
      }
      if (!map.containsKey(className)) {
        map.put(className, new ArrayList<String>());
      }
      if (!"".equals(jspFilePath) && (jspFilePath != null)) {
        map.get(className).add(jspFilePath);
      }
    }
    logger.info("jsp 파일 만들기 위한 데이터 가공 끝.");
    return map;
  }

  public static void createJsp(Map<String, List<String>> jspContentMap, String writeFilePath, String jTemplate) {

    logger.info("---------------------Create Jsp-------------------------------");

    for (String key : jspContentMap.keySet()) {
      List<String> list = jspContentMap.get(key);
      if (list.size() != 0) {
        try {

          for (String jspName : list) {
            String[] paths = jspName.split("/");
            String fullPath = "";
            for (String path : paths) {
              if (!path.endsWith(".jsp")) {
                fullPath += "/" + path;
                if (!FileUtil.existDirectory(writeFilePath + fullPath)) {
                  FileUtil.makeDirectory(writeFilePath + fullPath);
                }
              }
            }
            jTemplate = jTemplate.replaceAll("%fileName%", jspName);
            FileUtil.fileWrite(writeFilePath + jspName, jTemplate);

            if (FileUtil.existFile(writeFilePath + jspName)) {
              logger.info("jsp 파일 생성:  {}", new Object[] { jspName });
            }
          }
        } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        }

      }
    }
  }
}
