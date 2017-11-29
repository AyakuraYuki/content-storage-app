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
                "/two-step",
                "/two-step-auth",
                "/register-two-step",
                "/do-register-two-step",
                "/css/**",
                "/js/**",
                "/font/**")
        registry.addInterceptor(new DeniedResetTwoStepInterceptor())
                .addPathPatterns(
                "/two-step",
                "/two-step-auth",
                "/register-two-step",
                "/do-register-two-step")
        super.addInterceptors(registry)
    }

}
