package com.tena.untact2021.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tena.untact2021.dto.Article;
import com.tena.untact2021.dto.ResultData;
import com.tena.untact2021.service.ArticleService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserArticleController {

	private final ArticleService articleService;

	@ExceptionHandler
	@ResponseBody
	public ResultData userArticleExceptionHandler(Exception e) {
		return new ResultData(e.getClass().getSimpleName(), e.getMessage());
	}

	/* 게시물 조회 */
	@RequestMapping("/user/article/detail")
	@ResponseBody
	public Article showDetail(int id) {
		Article article = articleService.getArticle(id);
		return article;
	}

	/* 전체 게시물 조회 */
	@RequestMapping("/user/article/list")
	@ResponseBody
	public List<Article> showList(String searchKeywordType, String searchKeyword) {

		// searchKeywordType 미입력 시 titleAndBody를 기본값으로 셋팅
		if (searchKeywordType != null) {
			searchKeywordType = searchKeywordType.trim();
		}
		if (searchKeywordType == null || searchKeywordType.length() == 0) {
			searchKeywordType = "titleAndBody";
		}

		// Trim Whitespace
		if (searchKeyword != null) {
			searchKeyword = searchKeyword.trim();
		}

		// Empty String to Null
		if (searchKeyword != null && searchKeyword.length() == 0) {
			searchKeyword = null;
		}

		return articleService.getArticles(searchKeywordType, searchKeyword);
	}

	/* 게시물 추가 */
	@RequestMapping("/user/article/doAdd")
	@ResponseBody
	public ResultData doAdd(String title, String body) {
		if (title == null) {
			return new ResultData("F-1", "title을 입력해주세요.");
		}

		if (body == null) {
			return new ResultData("F-1", "body을 입력해주세요.");
		}

		return articleService.addArticle(title, body);
	}

	/* 게시물 삭제 */
	@RequestMapping("/user/article/doDelete")
	@ResponseBody
	public ResultData doDelete(Integer id) {
		if (id == null) {
			return new ResultData("F-1", "id를 입력해주세요.");
		}

		Article article = articleService.getArticle(id);

		if (article == null) {
			return new ResultData("F-1", "해당 게시물은 존재하지 않습니다.");
		}

		return articleService.deleteArticle(id);
	}

	/* 게시물 수정 */
	@RequestMapping("/user/article/doModify")
	@ResponseBody
	public ResultData doModify(Integer id, String title, String body) {
		if (id == null) {
			return new ResultData("F-1", "id를 입력해주세요.");
		}

		if (title == null) {
			return new ResultData("F-1", "title을 입력해주세요.");
		}

		if (body == null) {
			return new ResultData("F-1", "body을 입력해주세요.");
		}

		Article article = articleService.getArticle(id);

		if (article == null) {
			return new ResultData("F-1", "해당 게시물은 존재하지 않습니다.");
		}

		return articleService.modifyArticle(id, title, body);
	}

}