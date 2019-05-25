package %basePackageName%.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import %basePackageName%.config.Url;
import %basePackageName%.controller.bean.JsonResult;
import %basePackageName%.controller.bean.JsonResult.Code;
import %basePackageName%.util.PrettyLog;

@Controller
public class %className%Controller {
[여기에넣자]
  [메서드시작]
  /**
   * <pre>
   *  %description%
   * </pre>
   * 
   * @param request
   * @param response
   * @param modelMap
   */
  @RequestMapping(value = { %url% }, method = RequestMethod.%methodType%, produces = %produces%)
  %responseBody%
  public %returnType% %methodName%(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap, //
    PrettyLog prettyLog//
    ) {
    prettyLog.title("%description%");
    %return%;
  }
  [메서드끝]
}
