package com.tena.untact2021.config;

import com.tena.untact2021.dto.Member;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.SessionScope;

@Configuration
public class AppConfig {

    //로그인 성공 했을 경우 세션 영역에서 사용할 객체
    //Creating bean with name 'scopedTarget.loggedInMember'
    @Bean("loggedInMember")
    @SessionScope
    public Member loggedInMember() {
        Member member = new Member();
        return member;
    }

}
