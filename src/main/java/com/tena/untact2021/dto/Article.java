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
	private String extra__thumbUrl; //썸네일 링크

	private Map<String, Object> extra; //추가정보 맵핑에 사용할 필드

	// 추가정보 저장 시 사용할 편의 메소드
	public void addToExtra(String key, Object value) {
		getExtraNotNull().put(key, value);
	}

	@JsonIgnore
	public Map<String, Object> getExtraNotNull() {
		if (extra == null) extra = new HashMap<>();

		return extra;
	}

	@JsonIgnore
	private String fileIdsStr; //Ajax 요청으로 미리 저장된 게시물의 첨부 파일번호

	// 게시물 작성시 Ajax로 미리 업로드된 첨부파일이 있는지 유무 체크
	public boolean hasInputFiles() {
		return fileIdsStr != null && ! fileIdsStr.isBlank();
	}

}