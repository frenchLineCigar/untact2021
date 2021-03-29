package com.tena.untact2021.controller;

import java.util.List;
import java.util.Map;

import com.tena.untact2021.dto.Search;
import com.tena.untact2021.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tena.untact2021.dto.Article;
import com.tena.untact2021.dto.ResultData;
import com.tena.untact2021.service.ArticleService;

import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserArticleController {

	private final ArticleService articleService;

	@ExceptionHandler
	@ResponseBody
	public ResultData userArticleExceptionHandler(Exception e) {
		return new ResultData("Exception occurred.", "Bad Request");
//		return new ResultData(e.getClass().getSimpleName(), e.getMessage());
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
	public List<Article> showList(@ModelAttribute Search search) {
		log.info("UserArticleController.showList");
		log.info("search: {}", search);

		return articleService.getArticles(search.getSearchKeywordType(), search.getSearchKeyword());
	}

	@PostMapping(value = "/user/article/list", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Article> showListJson(@RequestBody Search search) {
		log.debug("UserArticleController.showListJson");
		log.debug("search: {}", search);

		return articleService.getArticles(search.getSearchKeywordType(), search.getSearchKeyword());
	}

	/* 게시물 추가 */
	@RequestMapping("/user/article/doAdd")
	@ResponseBody
	public ResultData doAdd(@RequestParam Map<String, Object> param, HttpSession session) {

        // 로그인 검증
        if (session.getAttribute("loggedInMemberId") == null) {
            return new ResultData("F-2", "로그인 후 이용해주세요.");
        }

		if (param.get("title") == null) {
			return new ResultData("F-1", "title을 입력해주세요.");
		}

		if (param.get("body") == null) {
			return new ResultData("F-1", "body을 입력해주세요.");
		}

        param.put("memberId", session.getAttribute("loggedInMemberId"));

		return articleService.addArticle(param);
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