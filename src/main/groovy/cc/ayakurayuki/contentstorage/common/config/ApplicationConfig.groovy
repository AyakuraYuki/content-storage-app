package cc.ayakurayuki.contentstorage.common.config

import cc.ayakurayuki.contentstorage.common.interceptor.AuthenticateInterceptor
import cc.ayakurayuki.contentstorage.common.interceptor.DeniedResetTwoStepInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * Created by Ayakura Yuki on 2017/10/19.
 */
@Configuration
class ApplicationConfig implements WebMvcConfigurer {

  @Override
  void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new AuthenticateInterceptor())
        .addPathPatterns("/**")
        .excludePathPatterns(
        "/error",
        "/2FA",
        "/access",
        "/register",
        "/doRegister2FA",
        "/doReset2FA",
        "/css/**",
        "/js/**",
        "/font/**")
    registry.addInterceptor(new DeniedResetTwoStepInterceptor())
        .addPathPatterns(
        "/2FA",
        "/access",
        "/register",
        "/doRegister2FA",
        "/doReset2FA")
  }

}
