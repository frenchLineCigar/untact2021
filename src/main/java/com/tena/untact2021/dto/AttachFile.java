package com.tena.untact2021.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
	private MultipartFile multipartFile;

	public boolean hasData() {
		return !this.multipartFile.isEmpty();
	}

	@JsonIgnore
	public String getFilePath(String fileDirPath) {
		return fileDirPath + getBaseFileUri();
	}

	@JsonIgnore
	private String getBaseFileUri() {
		return "/" + relTypeCode + "/" + fileDir + "/" + getFileName();
	}

	private String getFileName() {
		return id + "." + fileExt;
	}

	public String getForPrintUrl() {
		return "/file" + getBaseFileUri() + "?updateDate=" + updateDate;
	}
}
