package com.maumjido.generate.mybatis.source.util;

public class IfUtil {
  public static <T> T nvl(T target, T defaultValue) {
    if (target == null) {
      return defaultValue;
    } else {
      return target;
    }
  }

  public static String evl(String target, String defaultValue) {
    if (target == null || target.trim().equals("")) {
      return defaultValue;
    } else {
      return target;
    }
  }
}