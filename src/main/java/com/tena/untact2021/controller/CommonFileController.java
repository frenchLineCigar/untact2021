package com.tena.untact2021.controller;

import com.tena.untact2021.dto.ResultData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CommonFileController extends BaseController {

	@RequestMapping("/common/file/doUpload")
	@ResponseBody
	public ResultData doUpload() {

		//test
		return new ResultData("S-1", "업로드 성공", "fileIdsStr", "1,2");
	}

}