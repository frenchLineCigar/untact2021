package com.tena.untact2021.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    @Qualifier("commonInterceptor")
    HandlerInterceptor commonInterceptor;

    @Autowired
    @Qualifier("loginInterceptor")
    HandlerInterceptor loginInterceptor;

    @Autowired
    @Qualifier("logoutInterceptor")
    HandlerInterceptor logoutInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // commonInterceptor 인터셉터가 모든 요청 전에 실행되도록 처리
        registry.addInterceptor(commonInterceptor)
            .addPathPatterns("/**")
            .excludePathPatterns("/resource/**");

        // 로그인 필요
        // 일단 모든 요청에 적용 후, 로그인 필요없이 접속할 수 있는 URI를 excludePathPatterns 로 추가
        registry.addInterceptor(loginInterceptor)
            .addPathPatterns("/**")
            .excludePathPatterns("/")
            .excludePathPatterns("/resource/**")
            .excludePathPatterns("/user/home/main")
            .excludePathPatterns("/user/member/doLogin")
            .excludePathPatterns("/user/member/doJoin")
            .excludePathPatterns("/user/article/list")
            .excludePathPatterns("/user/article/detail")
            .excludePathPatterns("/user/reply/list")
            .excludePathPatterns("/test/**")
            .excludePathPatterns("/error");

        // 로그인 상태에서 접속할 수 없는 URI를 추가
        registry.addInterceptor(logoutInterceptor)
            .addPathPatterns("/user/member/doLogin")
            .addPathPatterns("/user/member/doJoin");
    }

}
