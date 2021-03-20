package com.tena.untact2021.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tena.untact2021.dao.ArticleDao;
import com.tena.untact2021.dto.Article;
import com.tena.untact2021.dto.ResultData;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleService {

	private final ArticleDao articleDao;

	/* 전체 게시물 조회 */
	public List<Article> getArticles(String searchKeywordType, String searchKeyword) {
		return articleDao.findAll(searchKeywordType, searchKeyword);
	}

	/* 게시물 조회 */
	public Article getArticle(int id) {
		return articleDao.findById(id);
	}


	/* 게시물 추가 */
	public ResultData addArticle(String title, String body) {
		int savedId = articleDao.save(title, body);

		return new ResultData("S-1", "성공하였습니다.", "id", savedId);
	}

	/* 게시물 삭제 */
	public ResultData deleteArticle(int id) {
		boolean result = articleDao.deleteById(id);

		if (result == false) {
			return new ResultData("F-1", "해당 게시물은 존재하지 않습니다.", "id", id);
		}

		return new ResultData("S-1", "삭제하였습니다.", "id", id);
	}

	/* 게시물 수정 */
	public ResultData modifyArticle(int id, String title, String body) {
		articleDao.update(id, title, body);

		return new ResultData("S-1", "게시물을 수정하였습니다.", "id", id);
	}

}
