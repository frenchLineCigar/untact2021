package com.tena.untact2021.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tena.untact2021.dao.ArticleDao;
import com.tena.untact2021.dto.Article;
import com.tena.untact2021.dto.ResultData;
import com.tena.untact2021.util.Util;

import lombok.RequiredArgsConstructor;

import static com.tena.untact2021.dto.Search.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleService {

	private final ArticleDao articleDao;

	/* 전체 게시물 조회 */
	public List<Article> getArticles(SearchKeywordType searchKeywordType, String searchKeyword) {
		return articleDao.findAll(searchKeywordType, searchKeyword);
	}

	/* 게시물 조회 */
	public Article getArticle(int id) {
		return articleDao.findById(id);
	}

	/* 게시물 추가 */
	public ResultData addArticle(Map<String, Object> param) {
		articleDao.save(param);

		int id = Util.getAsInt(param.get("id"));

		return new ResultData("S-1", "성공하였습니다.", "id", id);
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
