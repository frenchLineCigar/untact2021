package com.tena.untact2021.interceptor;

import com.tena.untact2021.dto.Member;
import com.tena.untact2021.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.tena.untact2021.dto.Member.AuthKeyStatus;

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

        Member currentMember = null;

        // 세션에 로그인한 유저의 요청인 경우
        if (loginMemberBean.isLogin()) {
            log.info("Session-based authentication.");

            currentMember = memberService.getMemberById(loginMemberBean.getId());
            currentMember.setLoginStatus(true);
            currentMember.setAuthKeyStatus(AuthKeyStatus.NONE);

            request.setAttribute("currentMember", currentMember);
        }

        // 인증키(authKey)가 있는 유저의 요청인 경우
        String authKey = request.getParameter("authKey");
        if (StringUtils.isNotBlank(authKey)) {
            currentMember = memberService.getMemberByAuthKey(authKey);

            if (currentMember != null) {
                log.info("Authenticated by using authKey.");
                currentMember.setLoginStatus(true);
                currentMember.setAuthKeyStatus(AuthKeyStatus.VALID);
            }
            else {
                currentMember = new Member();
                currentMember.setAuthKeyStatus(AuthKeyStatus.INVALID);
            }

            request.setAttribute("currentMember", currentMember);
        }

        // 둘 다 해당하지 않는 요청인 경우
        if (!loginMemberBean.isLogin() && StringUtils.isBlank(authKey)) {
            log.info("Not authenticated.");
            currentMember = new Member();
            currentMember.setLoginStatus(false);
            request.setAttribute("currentMember", currentMember);
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

}
