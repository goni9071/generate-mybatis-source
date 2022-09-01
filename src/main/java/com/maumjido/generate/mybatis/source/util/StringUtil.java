package com.maumjido.generate.mybatis.source.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.maumjido.generate.mybatis.source.common.Constants;

public class StringUtil {

  public static String convertCamelNaming(String source, boolean isFirstCharUpper) {
    if (source == null) {
      return source;
    }

    if (!"".equals(Constants.REMOVE_PREFIX_TABLENAME)) {
      String[] tokenList = Constants.REMOVE_PREFIX_TABLENAME.split(",");
      for (int i = 0; i < tokenList.length; i++) {
        if (StringUtil.isNotEmpty(tokenList[i])) {
          source = source.toLowerCase().replaceFirst("^" + tokenList[i].toLowerCase(), "");
        }
      }
    }

    StringBuffer result = new StringBuffer();
    source = source.toLowerCase();
    boolean isUpper = false;
    for (int i = 0; i < source.length(); i++) {
      char c = source.charAt(i);
      if (c == '_') {
        isUpper = true;
        continue;
      } else if (isUpper) {
        result.append(Character.toUpperCase(c));
        isUpper = false;
      } else if (isFirstCharUpper && i == 0) {
        result.append(Character.toUpperCase(c));
      } else {
        result.append(c);
      }
    }

    return result.toString();
  }


  public static boolean isEnglish(String txt) {
    boolean isEnglish = true;
    for (int i = 0; i < txt.length(); i++) {
      char c = txt.charAt(i);
      // 한글 ( 한글자 || 자음 , 모음 )
      if ((0xAC00 <= c && c <= 0xD7A3) || (0x3131 <= c && c <= 0x318E)) {
        isEnglish = false;
      } else if ((0x61 <= c && c <= 0x7A) || (0x41 <= c && c <= 0x5A)) {
        // 영어
        isEnglish = true;
      }
    }
    return isEnglish;
  }

  public static String ltrim(String source, String trimStr) {
    if (source != null && source.startsWith(trimStr)) {
      return source.substring(trimStr.length());
    }
    return source;
  }

  public static Map<String, List<String>> formatCheck(Map<String, List<String>> controllerContentMap) {

    Map<String, List<String>> map = new HashMap<String, List<String>>();

    for (String key : controllerContentMap.keySet()) {
      List<String> list = controllerContentMap.get(key);
      List<String> returnList = new ArrayList<String>();
      for (String content : list) {
        String[] split = content.split("\\n");
        String result = "";
        for (String txt : split) {
          if (!"".equals(txt.trim())) {
            result += txt + "\n";
            // System.out.println(txt);
          }
        }
        returnList.add(result);
      }
      map.put(key, returnList);
    }

    for (String key : controllerContentMap.keySet()) {
      List<String> list = controllerContentMap.get(key);
      if (!list.get(list.size() - 1).equals("}")) {
        list.add("}");
      }
    }

    return map;
  }

  public static String convertCamelNaming(String source) {
    return convertCamelNaming(source, true);
  }

  public static String convertDataType(String dataType) {
    if (dataType.toLowerCase().startsWith("char")) {
      dataType = "String";
    } else if (dataType.toLowerCase().startsWith("varchar")) {
      dataType = "String";
    } else if (dataType.toLowerCase().startsWith("text")) {
      dataType = "String";
    } else if (dataType.toLowerCase().startsWith("date")) {
      dataType = "Date";
    } else if (dataType.toLowerCase().startsWith("timestamp")) {
      dataType = "Date";
    } else if (dataType.toLowerCase().startsWith("bigint")) {
      dataType = "Long";
    } else if (dataType.toLowerCase().startsWith("int")) {
      dataType = "Integer";
    } else if (dataType.toLowerCase().startsWith("tinyint")) {
      dataType = "Integer";
    } else if (dataType.toLowerCase().startsWith("smallint")) {
      dataType = "Integer";
    } else if (dataType.toLowerCase().startsWith("float")) {
      dataType = "Float";
    } else if (dataType.toLowerCase().startsWith("double")) {
      dataType = "Double";
    } else if (dataType.toLowerCase().startsWith("number")) {
      if (dataType.contains("(")) {
        dataType = dataType.toLowerCase().replaceAll("\\(", "").replaceFirst("\\)", "").replaceAll("number", "");
        if (dataType.contains(",")) {
          if (Integer.valueOf(dataType.split(",")[0]) >= 19) {
            dataType = "Double";
          } else {
            dataType = "Float";
          }
        } else {
          if (Integer.valueOf(dataType) >= 19) {
            dataType = "Long";
          } else {
            dataType = "Integer";
          }
        }
      } else {
        dataType = "Integer";
      }
    } else {
      dataType = "String";
    }
    return dataType;
  }

  public static boolean isNotEmpty(String src) {
    return src != null && src.trim().length() > 0;
  }

  public static boolean isEmpty(String src) {
    return !isNotEmpty(src);
  }
}
