package com.tena.untact2021.controller;

import com.tena.untact2021.dto.Member;
import com.tena.untact2021.dto.ResultData;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class BaseControllerAdvice {

    @ModelAttribute("currentMember")
    public Member currentMember(HttpServletRequest request) {
        return (Member) request.getAttribute("currentMember");
    }

    @ExceptionHandler
    @ResponseBody
    public ResultData baseExceptionHandler(Exception e) {
        return new ResultData("Exception occurred.", "Bad Request");
//		return new ResultData(e.getClass().getSimpleName(), e.getMessage());
    }

}
