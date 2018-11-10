package cc.ayakurayuki.contentstorage.common.interceptor

import cc.ayakurayuki.contentstorage.common.base.Base
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * User authenticate interceptor
 *
 * @author ayakurayuki
 * @date 2017/10/19
 */
class AuthenticateInterceptor implements HandlerInterceptor {

  @Override
  boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
    if (Boolean.valueOf(String.valueOf(request.session.getAttribute(Base.AUTHENTIC)))) {
      return true
    }
    response.sendRedirect('/system/2FA')
    return false
  }

  @Override
  void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {
  }

  @Override
  void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {
  }

}
