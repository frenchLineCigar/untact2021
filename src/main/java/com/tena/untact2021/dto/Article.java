package com.tena.untact2021.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Article {
	private Integer id;
	private String regDate;
	private String updateDate;
    private Integer boardId;
    private Integer memberId;
	private String title;
	private String body;

	private String extra__writer; //작성자 (M.nickname)
	private String extra__boardName; //게시판 이름 (B.name)
	private String extra__thumbImg; //썸네일 링크

	private Map<Integer, AttachFile> fileMap; //첨부파일, Key(Integer) : fileNo

	@JsonIgnore
	private String fileIdsStr; //Ajax 요청으로 먼저 저장된 게시물의 첨부 파일번호

	public boolean hasInputFiles() {
		return fileIdsStr != null && ! fileIdsStr.isBlank();
	}

	@JsonIgnore
	public Map<Integer, AttachFile> getFileMapNotNull() {
		if (fileMap == null) fileMap = new HashMap<>();

		return fileMap;
	}

	public void setFileMapFromList(List<AttachFile> files) {
		// 입력 받은 파일 리스트를 fileMap 으로 가공 ( 맵의 key는 파일 순서(fileNo)로 할당 )
		if (files != null) {
			files.forEach(file -> this.getFileMapNotNull().put(file.getFileNo(), file));
		}
	}
}