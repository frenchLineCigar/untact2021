package com.tena.untact2021.custom;

import com.tena.untact2021.dto.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
public class CurrentMemberResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // @CurrentMember 붙어있는 파라미터에 바인딩 처리에만 사용
        return parameter.getParameterAnnotation(CurrentMember.class) != null
            && Member.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {

        // 요청 객체에 raw 하게 접근해 currentMember 속성을 리턴
        // 방법 1
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        Member currentMember = (Member) request.getAttribute("currentMember");
        // 방법 2
        // Member currentMember = (Member) webRequest.getAttribute("currentMember", 0);// SCOPE_REQUEST : 0
        // Member currentMember = (Member) webRequest.getAttribute("currentMember", RequestAttributes.SCOPE_REQUEST);// constant로 박을 때

        return currentMember;
    }
}
