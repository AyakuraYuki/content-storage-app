package cc.ayakurayuki.contentstorage.common.interceptor

import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by Ayakura Yuki on 2017/10/19.
 */

class AuthenticateInterceptor implements HandlerInterceptor {

    @Override
    boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        if (Boolean.valueOf(request.session.getAttribute('authentic').toString())) {
            return true
        }
        response.sendRedirect("/2FA")
        return false
    }

    @Override
    void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {

    }
}
