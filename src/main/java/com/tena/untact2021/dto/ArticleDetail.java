package com.tena.untact2021.dto;

import lombok.Data;

@Data
public class ArticleDetail {
	private Article article;
	private AttachFile thumbFile;

	public boolean hasArticle() {
		return article != null;
	}

	public boolean hasThumbFile() {
		return thumbFile != null;
	}

	// 썸네일 링크 셋팅용 편의 메서드
	public static void validAndValuateArticle(ArticleDetail articleDetail) {
		if (articleDetail.hasArticle() && articleDetail.hasThumbFile()) {
			articleDetail.getArticle().setExtra__thumbUrl(articleDetail.getThumbFile().getForPrintUrl());
		}
	}

}
