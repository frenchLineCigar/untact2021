package com.tena.untact2021.controller;

import com.tena.untact2021.util.Util;

import javax.servlet.http.HttpServletRequest;

/* In a nutshell, This class is VCG (View Code Generator) */
public class BaseController {

	/* 실패시 (for REST, i.e. Handler method with @ResponseBody) */
	protected String msgAndBack(String msg) {
		StringBuilder sb = new StringBuilder();
		sb.append("<script>");
		sb.append("alert('").append(msg).append("');");
		sb.append("history.back();"); //history.go(-1)
		sb.append("</script>");

		return sb.toString();
	}

	/* 실패시 (for VIEW) */
	protected String msgAndBack(HttpServletRequest req, String msg) {
		req.setAttribute("historyBack", true);
		req.setAttribute("msg", msg);
		return "common/redirect";
	}

	/* 성공시 */
	protected String msgAndReplace(String msg, String replaceUrl) {

		// 이동할 url이 없을 경우, 메인으로 이동
		replaceUrl = Util.ifEmpty(replaceUrl, "../home/main");

		StringBuilder sb = new StringBuilder();
		sb.append("<script>");
		sb.append("alert('").append(msg).append("');");
		sb.append("location.replace('").append(replaceUrl).append("');"); //PRG 패턴, 뒤로가기 시 중복 서브밋 방지
		sb.append("</script>");

		return sb.toString();
	}

}