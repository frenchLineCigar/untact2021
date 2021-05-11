package com.tena.untact2021.dto;

import com.tena.untact2021.util.Util;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

@Data
public class ResultData {

	@ApiModelProperty(position = 1, example = "S-1")
	private String resultCode;

	@ApiModelProperty(position = 2, example = "성공")
	private String msg;

	@ApiModelProperty(position = 3, example = "JSON 형태의 데이터가 리턴됩니다.")
	private Map<String, Object> body;

	public ResultData(String resultCode, String msg, Object... args) {
		this.resultCode = resultCode;
		this.msg = msg;
		this.body = Util.mapOf(args);
	}

    public ResultData(String resultCode, String msg, Map<String, Object> args) {
        this.resultCode = resultCode;
        this.msg = msg;
        this.body = args;
    }

	@ApiModelProperty(position = 4)
	public boolean isSuccess() {
		return resultCode.startsWith("S-");
	}

	@ApiModelProperty(position = 5)
	public boolean isFail() {
		return isSuccess() == false;
	}

}
