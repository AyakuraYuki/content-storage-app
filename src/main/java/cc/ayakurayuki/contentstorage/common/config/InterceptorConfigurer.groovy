package cc.ayakurayuki.contentstorage.common.config

import cc.ayakurayuki.contentstorage.common.interceptor.AuthenticateInterceptor
import cc.ayakurayuki.contentstorage.common.interceptor.DeniedResetTwoStepInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

/**
 * Created by Ayakura Yuki on 2017/10/19.
 */
@Configuration
class InterceptorConfigurer extends WebMvcConfigurerAdapter {

    @Override
    void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthenticateInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(
                "/error",
                "/2FA",
                "/do2FA",
                "/register2FA",
                "/doRegister2FA",
                "/doReset2FA",
                "/css/**",
                "/js/**",
                "/font/**")
        registry.addInterceptor(new DeniedResetTwoStepInterceptor())
                .addPathPatterns(
                "/2FA",
                "/do2FA",
                "/register2FA",
                "/doRegister2FA",
                "/doReset2FA",)
        super.addInterceptors(registry)
    }

}
