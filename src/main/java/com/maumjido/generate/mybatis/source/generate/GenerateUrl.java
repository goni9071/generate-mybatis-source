package com.maumjido.generate.mybatis.source.generate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maumjido.generate.mybatis.source.common.Constants;
import com.maumjido.generate.mybatis.source.util.FileUtil;
import com.maumjido.generate.mybatis.source.util.MenuUtil.Menu;
import com.maumjido.generate.mybatis.source.util.StringUtil;
import com.maumjido.generate.mybatis.source.util.TemplateUtil;

public class GenerateUrl {
  private static Logger logger = LoggerFactory.getLogger(GenerateUrl.class);

  public static void create(List<Menu> menuList) {

    // Data를 바탕으로 Url 내용 만들기
    String contents = GenerateUrl.getUrlContent(menuList, TemplateUtil.getTemplate("UrlTemplate.java"), Constants.PACKAGE_BASE);

    GenerateUrl.createController(contents, "result", Constants.PACKAGE_BASE);

  }

  public static String getUrlContent(List<Menu> menuList, String template, String packageName) {
    logger.info("--------------------------------------------------------");
    logger.info("Url 파일 만들기 위한 데이터 가공 시작.");

    StringBuffer methodTemplate = new StringBuffer();

    Map<String, Map<String, Map<String, String>>> classMap = new HashMap<>();
    for (Menu menu : menuList) {
      String urlCode = menu.getCode();
      String className = "";
      if (StringUtil.isNotEmpty(urlCode) && !urlCode.equalsIgnoreCase("null")) {
        className = urlCode.split("\\.")[0];
        if (!classMap.containsKey(className)) {
          classMap.put(className, new HashMap<String, Map<String, String>>());
        }
      } else {
        continue;
      }
      Map<String, Map<String, String>> varMap = classMap.get(className);
      String varCode = urlCode.split("\\.")[1];
      if (!varMap.containsKey(varCode)) {
        varMap.put(varCode, new HashMap<String, String>());
      }
      Map<String, String> subMap = varMap.get(varCode);
      String url = menu.getUrl();
      String jspFilePath = menu.getJsp();
      String comment = menu.getDescription();
      subMap.put("url", url);
      if (StringUtil.isNotEmpty(jspFilePath) && !jspFilePath.equalsIgnoreCase("null")) {
        subMap.put("jsp", jspFilePath.replaceAll(".jsp", ""));
      }
      subMap.put("comment", comment);
    }
    for (String className : classMap.keySet()) {
      methodTemplate.append(String.format("  public static final class %s {", className)).append("\n");
      Map<String, Map<String, String>> varMap = classMap.get(className);
      for (String varName : varMap.keySet()) {
        Map<String, String> subMap = varMap.get(varName);
        methodTemplate.append(String.format("    /**")).append("\n");
        methodTemplate.append(String.format("     * %s", subMap.get("comment"))).append("\n");
        methodTemplate.append(String.format("     */")).append("\n");
        methodTemplate.append(String.format("    public static final String %s = \"%s\";", varName, subMap.get("url"))).append("\n");
        if (subMap.containsKey("jsp")) {
          methodTemplate.append(String.format("    public static final String %s = \"%s\";", varName + "_JSP", subMap.get("jsp"))).append("\n");
        }
      }
      methodTemplate.append("  }").append("\n\n");
    }
    logger.info("Url 파일 만들기 위한 데이터 가공 끝.");
    return template.replaceAll("\\[여기에넣자\\]", methodTemplate.toString());
  }

  public static void createController(String contents, String writeFilePath, String packageName) {
    writeFilePath += "/config/";
    try {
      if (!FileUtil.existDirectory(writeFilePath)) {
        FileUtil.makeDirectory(writeFilePath);
      }

      FileUtil.fileWrite(writeFilePath + "Url.java", contents.replaceAll("%basePackageName%", packageName));

      if (FileUtil.existFile(writeFilePath + "Url.java")) {
        logger.info("Url 파일 생성 : {} ", "Url.java");
      }
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
