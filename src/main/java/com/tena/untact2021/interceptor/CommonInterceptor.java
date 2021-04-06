package com.tena.untact2021.interceptor;

import com.tena.untact2021.dto.Member;
import com.tena.untact2021.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * CommonInterceptor : 모든 요청에 대해 공통적으로 필요한 전처리 수행
 *  - 필터링 역할 x
 *  - 요청 정보 강화 및 표준화 역할 o
 */
@Slf4j
@Component("commonInterceptor")
public class CommonInterceptor implements HandlerInterceptor {

    @Autowired
    private MemberService memberService;

    @Resource(name = "loginMemberBean")
    private Member loginMemberBean; //세션영역에 로그인 정보를 담고있는 빈

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("CommonInterceptor.preHandle");

        // 로그인 된 사용자 정보를 통으로 request에 담아버림
        Member loginMember = null;
        if (loginMemberBean.isLogin()) {
            loginMember = memberService.getMemberById(loginMemberBean.getId());
        }
        request.setAttribute("loginMember", loginMember);

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

}
