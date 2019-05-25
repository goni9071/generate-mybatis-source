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

public class GenerateController {
  private static Logger logger = LoggerFactory.getLogger(GenerateController.class);

  private static final String REPLACE_METHOD_NAME = "%methodName%";
  private static final String REPACEL_RETURN_TYPE = "%returnType%";
  private static final String REPLACE_RETURN = "%return%";
  private static final String REPLACE_JSON_ANNOTATION = "%responseBody%";
  private static final String REPLACE_METHOD_TYPE = "%methodType%";
  private static final String REPLACE_URL = "%url%";
  private static final String REPLACE_DESCRIPTION = "%description%";
  private static final boolean useUrlCode = "Y".equals(Constants.OPTION_USE_URL_CODE);

  public static void create(List<Menu> menuList) throws UnsupportedEncodingException, IOException {

    // Data를 바탕으로 controller 내용 만들기
    Map<String/* Controller ClassName */ //
        , String /* Controller Contents */> controllerContentMap = GenerateController.getControllerContent(menuList,
            TemplateUtil.getTemplate("ControllerTemplate.java"), Constants.PACKAGE_BASE);

    // Map<String, List<String>> formatMap =
    // StringUtil.formatCheck(controllerContentMap);
    GenerateController.createController(controllerContentMap, "result");
    String jsonResultTemplate = TemplateUtil.getTemplate("JsonResultTemplate.java");
    jsonResultTemplate = jsonResultTemplate.replaceAll("%packageName%", Constants.PACKAGE_BASE);
    FileUtil.makeDirectory("result/controller/bean");
    FileUtil.fileWrite("result/controller/bean/JsonResult.java", jsonResultTemplate);
  }

  public static Map<String/* Controller ClassName */ //
      , String /* Controller Contents */> getControllerContent(List<Menu> menuList, String template,
          String packageName) {
    logger.info("--------------------------------------------------------");
    logger.info("Controller 파일 만들기 위한 데이터 가공 시작.");

    Map<String/* Controller ClassName */ //
        , String /* Controller Contents */> classContentsMap = new HashMap<String, String>();

    int methodStart = template.indexOf("[메서드시작]");
    int methodStop = template.indexOf("[메서드끝]");
    String methodTemplate = template.substring(methodStart + 8, methodStop);
    String base = template.substring(0, methodStart) + template.substring(methodStop + 6);

    for (Menu menu : menuList) {
      String url = menu.getUrl();
      String methodType = menu.getMethod();
      String contentType = menu.getContentsType();
      String urlCode = menu.getCode();
      String jsp = menu.getJsp();
      String methodName = methodType.toLowerCase();
      if (useUrlCode) {
        if (StringUtil.isEmpty(url) || StringUtil.isEmpty(urlCode) || urlCode.equalsIgnoreCase("null")) {
          continue;
        }
      } else {
        if (StringUtil.isEmpty(url)) {
          continue;
        }
      }

      String className = null;
      if ("/".equals(url)) {
        className = "Main";
      } else {
        className = StringUtil.convertCamelNaming(url.split("/")[1], true);
      }

      if (!classContentsMap.containsKey(className)) {
        classContentsMap.put(StringUtil.convertCamelNaming(className, true), base//
            .replaceAll("%className%", StringUtil.convertCamelNaming(className, true))//
            .replaceAll("%basePackageName%", Constants.PACKAGE_BASE)//
        );
      }
      if (useUrlCode) {
        methodName += StringUtil.convertCamelNaming(urlCode.toLowerCase().replaceAll("\\.", "_"), true);
      } else {
        methodName += StringUtil.convertCamelNaming(url.toLowerCase().replaceAll("/", "_"), true);
      }
      if ("JSON".equals(menu.getContentsType())) {
        methodName += "ByJson";
      }
      String contents = classContentsMap.get(className);
      if (useUrlCode) {
        contents = contents.replace("import %basePackageName%.config.Url", "");
      }
      contents = contents.replaceAll("\\[여기에넣자\\]", methodTemplate//
          .replaceAll(REPLACE_METHOD_TYPE, methodType)//
          .replaceAll("%produces%",
              "JSON".equals(contentType) ? "MediaType.APPLICATION_JSON_UTF8_VALUE" : "MediaType.TEXT_HTML_VALUE")//
          .replaceAll(REPLACE_JSON_ANNOTATION, "JSON".equals(contentType) ? "@ResponseBody" : "")//

          .replaceAll(REPACEL_RETURN_TYPE, "JSON".equals(contentType) ? "JsonResult" : "String")//
          .replaceAll(REPLACE_METHOD_NAME, methodName)//
          .replaceAll(REPLACE_DESCRIPTION, menu.getDescription())//
          + "\n[여기에넣자]"//
      );
      if (useUrlCode) {
        contents = contents
            .replaceAll(REPLACE_RETURN,
                !"JSON".equals(contentType) ? "return Url." + urlCode + "_JSP"
                    : "JsonResult jsonResult = new JsonResult();\n    jsonResult.setCode(Code.SUCC);\n    return jsonResult")//
            .replaceAll(REPLACE_URL, urlCode)//
        ;
      } else {
        contents = contents
            .replaceAll(REPLACE_RETURN,
                !"JSON".equals(contentType) ? ("return \"" + jsp.replaceAll(".jsp$", "") + "\"")
                    : "JsonResult jsonResult = new JsonResult();\n    jsonResult.setCode(Code.SUCC);\n    return jsonResult")//
            .replaceAll(REPLACE_URL, "\"" + url + "\"")//
        ;
      }
      classContentsMap.put(className, contents);
    }
    logger.info("Controller 파일 만들기 위한 데이터 가공 끝.");
    return classContentsMap;
  }

  public static void createController(Map<String/* Controller ClassName */ //
      , String /* Controller Contents */> classContentsMap, String writeFilePath) {
    writeFilePath += "/controller/";
    for (String className : classContentsMap.keySet()) {
      try {
        if (!FileUtil.existDirectory(writeFilePath)) {
          FileUtil.makeDirectory(writeFilePath);
        }

        FileUtil.fileWrite(writeFilePath + className + "Controller.java",
            classContentsMap.get(className).replaceAll("\\[여기에넣자\\]", ""));

        if (FileUtil.existFile(writeFilePath + className + "Controller.java")) {
          logger.info("Controller 파일 생성 : {} ", className + "Controller.java");
        }
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

  }

}
