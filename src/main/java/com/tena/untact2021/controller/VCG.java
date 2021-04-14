package com.tena.untact2021.controller;

/* View Code Generator */
public class VCG {

	/* 실패시 */
	protected String msgAndBack(String msg) {
		StringBuilder sb = new StringBuilder();
		sb.append("<script>");
		sb.append("alert('" + msg + "');");
		sb.append("history.back();"); //history.go(-1)
		sb.append("</script>");

		return sb.toString();
	}

	/* 성공시 */
	protected String msgAndReplace(String msg, String replaceUrl) {

		// 이동할 url이 없을 경우
		if (replaceUrl == null) {
			replaceUrl = "../home/main"; //default page
		}

		StringBuilder sb = new StringBuilder();
		sb.append("<script>");
		sb.append("alert('" + msg + "');");
		sb.append("location.replace('"+ replaceUrl +"');"); //PRG 패턴, 뒤로가기 시 중복 서브밋 방지
		sb.append("</script>");

		return sb.toString();
	}

}