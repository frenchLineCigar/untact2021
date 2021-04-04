package com.tena.untact2021.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article {
	private Integer id;
	private String regDate;
	private String updateDate;
    private Integer boardId;
    private Integer memberId;
	private String title;
	private String body;

	private String extra__writer; //작성자 (M.nickname)

}