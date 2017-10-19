package com.seeu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by neo on 17/01/2017.
 */
@Configuration
//@EnableWebMvc ??????????? mgj
public class InterceptorConfig extends WebMvcConfigurerAdapter {

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        // 配置 URL 匹配规则，后缀的点为参数，而不解析
        configurer.setUseSuffixPatternMatch(false).
                setUseTrailingSlashMatch(true);
    }

    @Bean
    public NormalQueryInterceptor normalQueryInterceptor() {
        return new NormalQueryInterceptor();
    }

    @Bean
    public HtmlPageInterceptor htmlPageInterceptor() {
        return new HtmlPageInterceptor();
    }

    /**
     * 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(normalQueryInterceptor())
                .addPathPatterns("/queryscorebyitem", "/mygpa0");
//        registry.addInterceptor(htmlPageInterceptor())
//                .addPathPatterns("/**")
//                .excludePathPatterns("/queryscorebyitem", "/mygpa0");
    }
}
