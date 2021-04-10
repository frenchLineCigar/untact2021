package com.tena.untact2021.dto;

import java.util.Map;

import org.springframework.lang.Nullable;

import com.tena.untact2021.util.Util;

import lombok.Data;

@Data
public class ResultData {

	private String resultCode;

	private String msg;

	private Map<String, Object> body;

	public ResultData(String resultCode, String msg, @Nullable Object... args) {
		this.resultCode = resultCode;
		this.msg = msg;
		this.body = Util.mapOf(args);
	}

    public ResultData(String resultCode, String msg, Map<String, Object> args) {
        this.resultCode = resultCode;
        this.msg = msg;
        this.body = args;
    }

	public boolean isSuccess() {
		return resultCode.startsWith("S-");
	}

	public boolean isFail() {
		return isSuccess() == false;
	}

}
