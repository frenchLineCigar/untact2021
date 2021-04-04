package com.tena.untact2021.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tena.untact2021.dto.Member;
import com.tena.untact2021.dto.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 로그인 필요 요청에 대해 먼저 로그인 여부 체크
 *
 * 로그인 필요 요청
 * - /user/member/doLogout
 * - /user/member/doModify
 * - /user/article/doAdd
 * - /user/article/doModify
 * - /user/article/doDelete
 */
@Component("loginInterceptor")
public class LoginInterceptor implements HandlerInterceptor {

    @Resource(name = "loginMemberBean")
    private Member loginMemberBean;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (loginMemberBean.isLogin() == false) {
            response.setContentType("application/json; charset=UTF-8");
            ResultData resultData = new ResultData("F-A", "로그인 후 이용해주세요.");
            String resultJson = objectMapper.writeValueAsString(resultData);
            response.getWriter().append(resultJson);

            return false;
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

}
