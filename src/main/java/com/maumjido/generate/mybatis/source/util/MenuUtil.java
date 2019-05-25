package com.maumjido.generate.mybatis.source.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

public class MenuUtil {
  public static List<Menu> getMenuList() throws InvalidFormatException, IOException {
    URL fileUrl = ClassLoader.getSystemResource("menu.xlsx");
    List<Menu> menuList = new ArrayList<Menu>();
    List<List<String>> rows = ExcelUtil.readExcel(new File(fileUrl.getFile()));
    rows.remove(0);
    String depth1 = "";
    String depth2 = "";
    String depth3 = "";
    String depth4 = "";
    for (List<String> row : rows) {
      Menu preMenu = null;
      if (menuList.size() > 0) {
        preMenu = menuList.get(menuList.size() - 1);
      }
      Menu menu = new Menu();
      depth1 = IfUtil.evl(row.get(0), depth1);
      if (preMenu != null && !depth1.equals(preMenu.getDepth1())) {
        depth2 = "";
        depth3 = "";
        depth4 = "";
      }
      depth2 = IfUtil.evl(row.get(1), depth2);
      if (preMenu != null && !depth2.equals(preMenu.getDepth2())) {
        depth3 = "";
        depth4 = "";
      }
      depth3 = IfUtil.evl(row.get(2), depth3);
      if (preMenu != null && !depth3.equals(preMenu.getDepth3())) {
        depth4 = "";
      }
      depth4 = IfUtil.evl(row.get(3), depth4);
      menu.setDepth1(depth1);
      menu.setDepth2(depth2);
      menu.setDepth3(depth3);
      menu.setDepth4(depth4);
      menu.setMethod(row.get(5));
      menu.setContentsType(row.get(6));
      menu.setUrl(row.get(7));
      menu.setJsp(row.get(8));
      if (row.size() > 9) {
        menu.setCode(row.get(9));
      }
      menuList.add(menu);
    }
    return menuList;
  }

  public static class Menu {
    private String depth1;
    private String depth2;
    private String depth3;
    private String depth4;
    private String method;
    private String contentsType;
    private String url;
    private String jsp;
    private String code;

    public String getDepth1() {
      return depth1;
    }

    public void setDepth1(String depth1) {
      this.depth1 = depth1;
    }

    public String getDepth2() {
      return depth2;
    }

    public void setDepth2(String depth2) {
      this.depth2 = depth2;
    }

    public String getDepth3() {
      return depth3;
    }

    public void setDepth3(String depth3) {
      this.depth3 = depth3;
    }

    public String getDepth4() {
      return depth4;
    }

    public void setDepth4(String depth4) {
      this.depth4 = depth4;
    }

    public String getMethod() {
      return method;
    }

    public void setMethod(String method) {
      this.method = method;
    }

    public String getContentsType() {
      return contentsType;
    }

    public void setContentsType(String contentsType) {
      this.contentsType = contentsType;
    }

    public String getUrl() {
      return url;
    }

    public void setUrl(String url) {
      this.url = url;
    }

    public String getJsp() {
      return jsp;
    }

    public void setJsp(String jsp) {
      this.jsp = jsp;
    }

    public String getCode() {
      return code;
    }

    public void setCode(String code) {
      this.code = code;
    }

    public String getDescription() {
      String description = getDepth1();
      if (StringUtil.isNotEmpty(getDepth2())) {
        description += " > " + getDepth2();
      }
      if (StringUtil.isNotEmpty(getDepth3())) {
        description += " > " + getDepth3();
      }
      if (StringUtil.isNotEmpty(getDepth4())) {
        description += " > " + getDepth4();
      }
      return description;
    }
  }

}
