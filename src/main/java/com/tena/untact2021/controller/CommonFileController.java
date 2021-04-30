package com.tena.untact2021.controller;

import com.tena.untact2021.dto.ResultData;
import com.tena.untact2021.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;

import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CommonFileController extends BaseController {

	private final FileService fileService;

	/* Ajax 파일 업로드  */
	@RequestMapping("/common/file/doUpload")
	@ResponseBody
	public ResultData doUpload(MultipartRequest multipartRequest, @RequestParam Map<String, Object> paramMap) {

		return fileService.saveFiles(multipartRequest, paramMap);
	}

}