package com.tena.untact2021.controller;

import com.tena.untact2021.dto.AttachFile;
import com.tena.untact2021.dto.ResultData;
import com.tena.untact2021.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CommonFileController extends BaseController {

	private final FileService fileService;

	@Value("${custom.fileDirPath}")
	private String fileDirPath;

	/* Ajax 파일 업로드  */
	@RequestMapping("/common/file/doUpload")
	@ResponseBody
	public ResultData doUpload(MultipartRequest multipartRequest, @RequestParam Map<String, Object> paramMap) {

		return fileService.saveFiles(multipartRequest, paramMap);
	}

	@RequestMapping(value = "/file/download")
	@ResponseBody
	public void getImageAsByteArray(int id, HttpServletResponse response, HttpServletRequest request) throws IOException {
		AttachFile found = fileService.getFileById(id);
		String fileRealPath = found.getFilePath(fileDirPath);

		// 해당 파일의 인풋 스트림 생성
		InputStream in = new FileInputStream(fileRealPath);

		// Content-Type 추출
		String contentType= URLConnection.guessContentTypeFromName(fileRealPath);
		// String contentType = request.getServletContext().getMimeType(fileRealPath);

		// Content-Type 헤더 설정
		response.setContentType(contentType);
		// response.setHeader("Content-Type", contentType);

		// Content-Disposition 헤더를 attachment 로 설정하여 다운로드 받을 파일임을 브라우저에게 알려줌
		response.setHeader("Content-Disposition", "attachment; filename=\"" + found.getOriginFileName() + "\"");

		FileCopyUtils.copy(in, response.getOutputStream());
	}

	@RequestMapping(value = "/test/download/v1", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
	public @ResponseBody byte[] getImageAsByteArrayTest_V1(int id, HttpServletResponse response) throws IOException {
		AttachFile found = fileService.getFileById(id);
		String fileRealPath = found.getFilePath(fileDirPath);

		// 해당 파일의 인풋 스트림 생성
		InputStream in = new FileInputStream(fileRealPath);

		// Content-Disposition 헤더를 attachment 로 설정하여 다운로드 받을 파일임을 브라우저에게 알려줌
		response.setHeader("Content-Disposition", "attachment; filename=\"" + found.getOriginFileName() + "\"");

		return IOUtils.toByteArray(in); // produces 에 명시한 타입으로 Content-Type 헤더가 설정됨
	}

	// 원시 파일(raw file)로 퉁치려면 APPLICATION_OCTET_STREAM_VALUE
	@RequestMapping(value = "/test/download/v2", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public @ResponseBody byte[] getImageAsByteArray_Test_V2(int id, HttpServletResponse response) throws IOException {
		AttachFile found = fileService.getFileById(id);
		String fileRealPath = found.getFilePath(fileDirPath);

		// 해당 파일의 인풋 스트림 생성
		InputStream in = new FileInputStream(fileRealPath);

		// Content-Disposition 헤더를 attachment 로 설정하여 다운로드 받을 파일임을 브라우저에게 알려줌
		response.setHeader("Content-Disposition", "attachment; filename=\"" + found.getOriginFileName() + "\"");

		return IOUtils.toByteArray(in); // produces 에 명시한 타입으로 Content-Type 헤더가 설정됨
	}

}