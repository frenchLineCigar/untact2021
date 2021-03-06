package com.tena.untact2021.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tena.untact2021.util.Util;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttachFile {
	// Format : file__[relTypeCode]__[relId]__[typeCode]__[type2Code]__[fileNo]
	// Ex) file__article__0__common__attachment__1
	private int id;
	private String regDate;
	private String updateDate;
	private boolean delStatus;
	private String delDate;
	private String relTypeCode; //연관 데이터 타입 (Ex. article)
	private int relId; //연관 데이터 번호
	private String typeCode; //파일 종류 코드 (Ex. common)
	private String type2Code; //파일 종류2 코드 (Ex. attachment)
	private int fileNo; //파일 번호 (1 or 2) - 몇번째 첨부파일인지
	private int fileSize; //파일 크기(byte)
	private String originFileName; //원본 파일명
	private String fileExtTypeCode; //파일 규격 코드 (Ex. img, video)
	private String fileExtType2Code; //파일 규격2 코드 (Ex. jpg, mp4)
	private String fileExt; //파일 확장자
	private String fileDir; //파일 저장 폴더 : yyyy_MM (Ex. 2021_03)

	/* 단위 환산된 파일 크기 리턴 (byte, KB, MB) */
	public String getFileSizeWithUnit() {
		return Util.byteSizeTo(fileSize, 2);
	}

	/* 실제 파일이 저장된 서버 경로 */
	@JsonIgnore
	public String getFilePath(String fileDirPath) {
		return fileDirPath + getBaseFileUri(); // -> /2021/untact2021-file/article/2021_04/1.png
	}

	@JsonIgnore
	private String getBaseFileUri() {
		return "/" + relTypeCode + "/" + fileDir + "/" + getFileName();
	}

	public String getFileName() {
		return id + "." + fileExt;
	}

	public String getForPrintUrl() {
		return "/file" + getBaseFileUri() + "?updateDate=" + updateDate;
	}

	public String getDownloadUrl() {
		return "/common/file/doDownload?id=" + id;
	}

	public String getMediaHtml() {
		return "<img src=\""+ getForPrintUrl() + "\">";
	}

	/* Map MultipartFile To AttachFile */
	public static AttachFile from(MultipartFile multipartFile) {
		String paramName = multipartFile.getName(); // file__article__5__common__attachment__1
		String[] paramNameBits = paramName.split("__"); // ["file", "article", "5", "common", "attachment", "1"]

		AttachFile attachFile = AttachFile.builder()
				.relTypeCode(paramNameBits[1]) // article
				.relId(Integer.parseInt(paramNameBits[2])) // 5 (연관 게시물 번호)
				.typeCode(paramNameBits[3]) // common
				.type2Code(paramNameBits[4]) // attachment
				.fileNo(Integer.parseInt(paramNameBits[5])) // 1 (첨부파일 순서)
				.fileSize((int) multipartFile.getSize())
				.originFileName(multipartFile.getOriginalFilename())
				.fileExtTypeCode(Util.getFileExtTypeCodeFromFileName(multipartFile.getOriginalFilename()))
				.fileExtType2Code(Util.getFileExtType2CodeFromFileName(multipartFile.getOriginalFilename()))
				.fileExt(Util.getFileExtFromFileName(multipartFile.getOriginalFilename()))
				.fileDir(Util.getNowYearMonthDateStr())
				.build();

		return attachFile;
	}

}
