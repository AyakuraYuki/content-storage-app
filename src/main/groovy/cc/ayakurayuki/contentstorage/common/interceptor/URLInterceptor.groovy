package cc.ayakurayuki.contentstorage.common.interceptor

import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 *
 * @author ayakurayuki
 * @date 2018/11/10-16:32
 */
class URLInterceptor implements HandlerInterceptor {

  @Override
  boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
    println request.requestURL
    return true
  }

  @Override
  void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {
  }

  @Override
  void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {
  }

}
