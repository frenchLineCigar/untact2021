package com.tena.untact2021.controller;

import com.tena.untact2021.dto.Article;
import com.tena.untact2021.dto.Member;
import com.tena.untact2021.dto.ResultData;
import com.tena.untact2021.dto.Search;
import com.tena.untact2021.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * TODO : 리팩토링 해야할 것
 *  -> Interceptor 처리 : 로그인 검증, 권한 체크
 *  -> 바인딩 및 유효성 검증 : JSR-303 사용(DTO) 및 Validator 구현(@InitBinder) + HandlerMethodArgumentResolver
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class UserArticleController {

	private final ArticleService articleService;

    @Resource(name = "loggedInMember")
    private Member loggedInMember;

	@ExceptionHandler
	@ResponseBody
	public ResultData userArticleExceptionHandler(Exception e) {
		return new ResultData("Exception occurred.", "Bad Request");
//		return new ResultData(e.getClass().getSimpleName(), e.getMessage());
	}

    /* 게시물 조회 */
	@RequestMapping("/user/article/detail")
	@ResponseBody
	public ResultData showDetail(Integer id) {

        if (id == null) {
            return new ResultData("F-1", "id를 입력해주세요.");
        }

		Article article = articleService.getForPrintArticle(id);

        if (article == null) {
            return new ResultData("F-2", "존재하지 않는 게시물입니다.");
        }
		return new ResultData("S-1", "조회 결과", "article", article);
	}

	/* 전체 게시물 조회 */
	@RequestMapping("/user/article/list")
	@ResponseBody
	public ResultData showList(@ModelAttribute Search search) {
		log.info("UserArticleController.showList");
		log.info("search: {}", search);

        List<Article> articles = articleService.getForPrintArticle(search.getSearchKeywordType(), search.getSearchKeyword());
        return new ResultData("S-1", "조회 결과", "articles", articles);
	}

	@PostMapping(value = "/user/article/list", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResultData showListJson(@RequestBody Search search) {
		log.debug("UserArticleController.showListJson");
		log.debug("search: {}", search);

        List<Article> articles = articleService.getForPrintArticle(search.getSearchKeywordType(), search.getSearchKeyword());
        return new ResultData("S-1", "조회 결과", "articles", articles);
	}

	/* 게시물 추가 */
	@RequestMapping("/user/article/doAdd")
	@ResponseBody
	public ResultData doAdd(Article article) {

        // 로그인 검증
        if (!loggedInMember.isLogin()) {
            return new ResultData("F-2", "로그인 후 이용해주세요.");
        }

		if (article.getTitle()== null) {
			return new ResultData("F-1", "title을 입력해주세요.");
		}

		if (article.getBody() == null) {
			return new ResultData("F-1", "body을 입력해주세요.");
		}

		return articleService.addArticle(article);
	}

	/* 게시물 삭제 */
	@RequestMapping("/user/article/doDelete")
	@ResponseBody
	public ResultData doDelete(Integer id) {

        // 로그인 검증
        if (!loggedInMember.isLogin()) {
            return new ResultData("F-2", "로그인 후 이용해주세요.");
        }

		if (id == null) {
			return new ResultData("F-1", "id를 입력해주세요.");
		}

		return articleService.deleteArticle(id);
	}

	/* 게시물 수정 */
	@RequestMapping("/user/article/doModify")
	@ResponseBody
	public ResultData doModify(Article article) {

        // 로그인 검증
        if (!loggedInMember.isLogin()) {
            return new ResultData("F-2", "로그인 후 이용해주세요.");
        }
        
		if (article.getId() == null) {
			return new ResultData("F-1", "id를 입력해주세요.");
		}

		if (article.getTitle() == null) {
			return new ResultData("F-1", "title을 입력해주세요.");
		}

		if (article.getBody() == null) {
			return new ResultData("F-1", "body을 입력해주세요.");
		}

        return articleService.modifyArticle(article);
	}

}