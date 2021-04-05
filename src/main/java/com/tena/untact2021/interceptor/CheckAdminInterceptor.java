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

@Slf4j
@Component("checkAdminInterceptor")
public class CheckAdminInterceptor implements HandlerInterceptor {

    @Resource(name = "loginMemberBean")
    private Member loginMemberBean;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("CheckAdminInterceptor.preHandle");

        if (! loginMemberBean.isAdmin()) {
            response.setContentType("application/json; charset=UTF-8");
            ResultData resultData = new ResultData("F-B", "관리자로 로그인 후 다시 이용해주세요.");
            String resultJson = objectMapper.writeValueAsString(resultData);
            response.getWriter().append(resultJson);

            return false;
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

}
