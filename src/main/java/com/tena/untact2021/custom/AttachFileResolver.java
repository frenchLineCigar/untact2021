package com.tena.untact2021.custom;

import com.tena.untact2021.dto.AttachFile;
import com.tena.untact2021.util.Util;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 첨부 파일 리졸버
 */
@Component
public class AttachFileResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return List.class.isAssignableFrom(parameter.getParameterType())
				&& ((ParameterizedType) parameter.getGenericParameterType()).getActualTypeArguments()[0] == AttachFile.class;
//		System.out.println("parameter.getMember() = " + parameter.getMember());
//		System.out.println("parameter.getMethod() = " + parameter.getMethod());
//		System.out.println("parameter.getMethod().getName() = " + parameter.getMethod().getName());
//		System.out.println("parameter.getParameterName() = " + parameter.getParameterName());
//		System.out.println("parameter.getParameterType() = " + parameter.getParameterType());
//		System.out.println("genericParameterType = " + parameter.getGenericParameterType());
//		System.out.println("nestedGenericParameterType = " + parameter.getNestedGenericParameterType());
//		System.out.println("parameter.getParameterType().equals(List.class) = " + parameter.getParameterType().equals(List.class));
//		System.out.println("List.class.isAssignableFrom(parameter.getParameterType()) = " + List.class.isAssignableFrom(parameter.getParameterType()));
//		return List.class.isAssignableFrom(parameter.getParameterType()) && parameter.getParameterName().equals("attachFiles")
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		MultipartHttpServletRequest request = (MultipartHttpServletRequest) webRequest.getNativeRequest();

		//결과를 담아 파라미터에 바인딩 할 객체
		List<AttachFile> attachFiles = new ArrayList<>();

		//현재 요청의 첨부파일 정보가 담긴 Map
		Map<String, MultipartFile> fileMap = request.getFileMap();
//		fileMap.entrySet().forEach(System.out::println);

		//첨부 파일 정보 추출
		for (String fileInputName : fileMap.keySet()) {
			MultipartFile multipartFile = fileMap.get(fileInputName);

			String[] fileInputNameBits = fileInputName.split("__");
			// Ex) file__article__0__common__attachment__1
			// => ["file", "article", "0", "common", "attachment", "1"]
			// fileInputName : "file__{relTypeCode}__{relId}__{typeCode}__{type2Code}__{fileNo}"
			// fileInputNameBits : ["file", {relTypeCode}, {relId}, {typeCode}, {type2Code}, {fileNo}]

			// file 로 시작하지 않으면 skip
			if (fileInputNameBits[0].equals("file") == false) continue;

			// size 가 0 이하면 skip
			int fileSize = (int) multipartFile.getSize();
			if (fileSize <= 0) continue;

			// 파일 메타 정보 가공
			AttachFile attachFile = AttachFile.builder()
					.relTypeCode(fileInputNameBits[1])
					.relId(Integer.parseInt(fileInputNameBits[2]))
					.typeCode(fileInputNameBits[3])
					.type2Code(fileInputNameBits[4])
					.fileNo(Integer.parseInt(fileInputNameBits[5]))
					.fileSize(fileSize)
					.originFileName(multipartFile.getOriginalFilename())
					.fileExtTypeCode(Util.getFileExtTypeCodeFromFileName(multipartFile.getOriginalFilename()))
					.fileExtType2Code(Util.getFileExtType2CodeFromFileName(multipartFile.getOriginalFilename()))
					.fileExt(Util.getFileExtFromFileName(multipartFile.getOriginalFilename()))
					.fileDir(Util.getNowYearMonthDateStr())
					.build();

			// 메타 정보 저장
			attachFiles.add(attachFile);
		}

		return attachFiles;
	}
}
