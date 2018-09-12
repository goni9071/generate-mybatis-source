package %basePackageName%.config;

public class Url {

  public static String removePathVariable(String url) {
    return url.replaceAll("/\\{[^\\}]+\\}", "");
  }

  [여기에넣자]
}