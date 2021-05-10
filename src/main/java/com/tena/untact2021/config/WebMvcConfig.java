package com.tena.untact2021.config;

import com.tena.untact2021.custom.CurrentMemberResolver;
import com.tena.untact2021.interceptor.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired private CommonInterceptor commonInterceptor;
    @Autowired private CheckLoginInterceptor checkLoginInterceptor;
    @Autowired private CheckLogoutInterceptor checkLogoutInterceptor;
    @Autowired private CheckWriterInterceptor checkWriterInterceptor;
    @Autowired private CheckAdminInterceptor checkAdminInterceptor;
    @Autowired private CurrentMemberResolver currentMemberResolver;

    @Value("${custom.fileDirPath}")
    private String fileDirPath;

    /* 앱과 통신하기 위해 CORS 허용 */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }

    /* @CurrentMember 애노테이션 사용 시, 현재 인증된 사용자 정보 바인딩 */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(currentMemberResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) { // 인터셉터는 등록된 순서대로 적용
        // commonInterceptor 인터셉터가 모든 요청 전에 실행되도록 처리
        registry.addInterceptor(commonInterceptor)
            .addPathPatterns("/**")
            .excludePathPatterns("/resource/**")
            .excludePathPatterns("/file/**"); // 서버 리소스 접근 (앱 외부 디렉터리)

        // 관리자 권한 필요
        registry.addInterceptor(checkAdminInterceptor)
            .addPathPatterns("/admin/**")
            .excludePathPatterns("/admin/member/login").excludePathPatterns("/admin/member/doLogin") // 어드민 로그인
            .excludePathPatterns("/admin/member/join").excludePathPatterns("/admin/member/doJoin"); // 어드민 가입

        // 로그인 필요
        // 일단 모든 요청에 적용 후, 로그인 필요없이 접속할 수 있는 URI를 excludePathPatterns 로 추가
        registry.addInterceptor(checkLoginInterceptor)
            .addPathPatterns("/**")
            .excludePathPatterns("/")
            .excludePathPatterns("/admin/**") // 어드민 관련 요청은 위 checkAdminInterceptor에서 잡음
            .excludePathPatterns("/resource/**")
            .excludePathPatterns("/file/**") // 서버 리소스 접근 (앱 외부 디렉터리)
            .excludePathPatterns("/user/home/**")
            .excludePathPatterns("/user/member/authKey")
            .excludePathPatterns("/user/member/login")
            .excludePathPatterns("/user/member/doLogin")
            .excludePathPatterns("/user/member/join")
            .excludePathPatterns("/user/member/doJoin")
            .excludePathPatterns("/user/article/list")
            .excludePathPatterns("/user/article/detail")
            .excludePathPatterns("/user/reply/list")
            .excludePathPatterns("/test/**")
            .excludePathPatterns("/error")
            .excludePathPatterns("/swagger-resources/**")
            .excludePathPatterns("/swagger-ui/**") // swagger v3 사용시
            //.excludePathPatterns("/swagger-ui.html") // swagger v2 사용시
            .excludePathPatterns("/v2/api-docs")
            .excludePathPatterns("/webjars/**");

        // 로그인 상태에서 접속할 수 없는 URI를 추가
        // 로그인 상태에서는 로그인 요청을 반복하거나 가입 요청을 할 수 없도록 로그아웃 여부 먼저 체크
        registry.addInterceptor(checkLogoutInterceptor)
            .addPathPatterns("/admin/member/login")
            .addPathPatterns("/admin/member/doLogin")
            .addPathPatterns("/user/member/login")
            .addPathPatterns("/user/member/doLogin")
            .addPathPatterns("/user/member/join")
            .addPathPatterns("/user/member/doJoin");

        // 컨텐츠 수정, 삭제 권한 검증
        registry.addInterceptor(checkWriterInterceptor)
            .addPathPatterns("/admin/article/doModify")
            .addPathPatterns("/admin/article/doDelete")
            .addPathPatterns("/user/article/doModify")
            .addPathPatterns("/user/article/doDelete")
            .addPathPatterns("/user/reply/doModify")
            .addPathPatterns("/user/reply/doDelete");
    }

    /* 리소스 핸들러 설정 */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /* 서버 리소스(앱 외부 디렉터리) 요청 처리 */
        registry.addResourceHandler("/file/**")
                .addResourceLocations("file://" + fileDirPath + "/").setCachePeriod(20); //캐시 지속시간 20초
    }

}
