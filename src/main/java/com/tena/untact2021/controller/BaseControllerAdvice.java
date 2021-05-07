package com.tena.untact2021.controller;

import com.tena.untact2021.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;

@Slf4j
@ControllerAdvice
public class BaseControllerAdvice {

	/**
	 * MultipartException 처리
	 */
	@ExceptionHandler(value = MultipartException.class)
	public ResponseEntity<?> handleMultipartException(MultipartException e) {
		String errorMsg = e.getMessage();
		e.printStackTrace();
		log.error("MultipartException");

		if (e.getRootCause() instanceof SizeLimitExceededException) {
			// the request size exceeds the configured maximum (크기 파일 사이즈 초과 시 톰캣에서 뱉음)
			SizeLimitExceededException rootEx = (SizeLimitExceededException) e.getRootCause();
			log.error("message [" + rootEx.getMessage() + "]");
			log.error("MaxUploadSize [" + rootEx.getPermittedSize() + "]");
			errorMsg = "업로드 가능한 파일 용량을 초과하였습니다. \n";
			errorMsg += "최대 파일 크기  [ " + (Util.byteSizeTo(rootEx.getPermittedSize())) + " ] ";
			// errorMsg += "최대 파일 크기  [ " + (rootEx.getPermittedSize() / 1024) + "KB ] ";

			return new ResponseEntity<>(errorMsg, HttpStatus.PAYLOAD_TOO_LARGE);
			// return new ResponseEntity<>(errorMsg, HttpStatus.BAD_REQUEST);
		} else {
			log.error("message [" + errorMsg + "]");
			errorMsg = "파일업로드중 문제가 발생하였습니다.";

			return new ResponseEntity<>(errorMsg, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

//    @ExceptionHandler
//    @ResponseBody
//    public ResultData baseExceptionHandler(Exception e) {
//        return new ResultData("Exception occurred.", "Bad Request");
////		return new ResultData(e.getClass().getSimpleName(), e.getMessage());
//    }

}
