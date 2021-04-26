package com.tena.untact2021.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

	@JsonIgnore
	private String fileIdsStr; //Ajax 요청으로 먼저 저장된 게시물의 첨부 파일번호

	public boolean hasUploadedFiles() {
		return fileIdsStr != null && ! fileIdsStr.isBlank();
	}

}