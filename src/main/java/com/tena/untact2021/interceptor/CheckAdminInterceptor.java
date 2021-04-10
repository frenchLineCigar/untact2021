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
 * CheckAdminInterceptor
 * - 어드민 여부 필터링
 * - 관리자 권한이 필요한 요청에 대해 먼저 자격 여부 체크
 */
@Slf4j
@Component("checkAdminInterceptor")
public class CheckAdminInterceptor implements HandlerInterceptor {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("CheckAdminInterceptor.preHandle");

        Member currentMember = (Member) request.getAttribute("currentMember");

        if (! currentMember.isAdmin()) {
            ResultData resultData = new ResultData("F-B", "관리자로 로그인 후 다시 이용해주세요.");
            String resultJson = objectMapper.writeValueAsString(resultData);
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().append(resultJson);

            return false;
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

}
