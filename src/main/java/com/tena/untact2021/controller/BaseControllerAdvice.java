package com.tena.untact2021.controller;

import com.tena.untact2021.dto.ResultData;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class BaseControllerAdvice {

    @ExceptionHandler
    @ResponseBody
    public ResultData baseExceptionHandler(Exception e) {
        return new ResultData("Exception occurred.", "Bad Request");
//		return new ResultData(e.getClass().getSimpleName(), e.getMessage());
    }

}
