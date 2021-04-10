package com.tena.untact2021.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tena.untact2021.dto.Member;
import com.tena.untact2021.dto.ResultData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * CheckLogoutInterceptor
 *  - 로그아웃이 우선 선행되어야 할 요청에 대해 로그아웃 여부 체크
 */
@Slf4j
@Component("checkLogoutInterceptor")
public class CheckLogoutInterceptor implements HandlerInterceptor {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("CheckLogoutInterceptor.preHandle");

        Member currentMember = (Member) request.getAttribute("currentMember");

        if (currentMember.isLogin()) {
            ResultData resultData = new ResultData("F-A", "로그아웃 상태에서 이용해주세요.");
            String resultJson = objectMapper.writeValueAsString(resultData);
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().append(resultJson);

            return false;
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

}
