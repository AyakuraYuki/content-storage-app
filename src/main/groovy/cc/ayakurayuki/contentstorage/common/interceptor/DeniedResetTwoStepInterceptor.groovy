package cc.ayakurayuki.contentstorage.common.interceptor

import cc.ayakurayuki.contentstorage.common.base.BaseBean
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author ayakurayuki
 * @date 2017/10/19
 */
class DeniedResetTwoStepInterceptor implements HandlerInterceptor {

  @Override
  boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    if (Boolean.valueOf(String.valueOf(request.session.getAttribute(BaseBean.AUTHENTIC)))) {
      response.sendRedirect('/content/')
      return false
    }
    return true
  }

  @Override
  void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
  }

  @Override
  void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
  }

}
