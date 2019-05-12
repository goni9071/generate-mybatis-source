package com.maumjido.generate.mybatis.source.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SystemProperties {
  private static SystemProperties systemProperties = new SystemProperties();

  private Properties properties = null;

  public static String getProperty(String key) {
    String property = systemProperties.properties.getProperty(key);
    if (property == null)
      throw new RuntimeException(key + " can not found.");
    return property;
  }

  public static String getProperty(String key, String defaultValue) {
    String property = systemProperties.properties.getProperty(key);
    if (property == null)
      return defaultValue;
    return property;
  }

  public SystemProperties() {
    properties = new Properties();
    InputStream is = SystemProperties.class.getResourceAsStream("/config.properties");
    try {
      properties.load(is);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public Properties getProperties() {
    return properties;
  }

  public void setProperties(Properties properties) {
    this.properties = properties;
  }

  public static void main(String[] args) {
  }
}
