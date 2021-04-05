package com.tena.untact2021.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tena.untact2021.dto.Member;
import com.tena.untact2021.dto.ResultData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 로그아웃이 필요한 요청에 대해 로그아웃 여부 체크
 * 
 * 로그아웃이 먼저 필요한 요청
 * - /user/member/doLogin
 * - /user/member/doJoin
 */
@Slf4j
@Component("logoutInterceptor")
public class LogoutInterceptor implements HandlerInterceptor {

    @Resource(name = "loginMemberBean")
    private Member loginMemberBean;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("LogoutInterceptor.preHandle");

        if (loginMemberBean.isLogin()) {
            response.setContentType("application/json; charset=UTF-8");
            ResultData resultData = new ResultData("F-A", "로그아웃 상태에서 이용해주세요.");
            String resultJson = objectMapper.writeValueAsString(resultData);
            response.getWriter().append(resultJson);

            return false;
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

}
