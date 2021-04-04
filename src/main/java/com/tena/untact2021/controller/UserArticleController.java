package com.tena.untact2021.controller;

import com.tena.untact2021.dto.Article;
import com.tena.untact2021.dto.Board;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

import static com.tena.untact2021.dto.Search.*;

/**
 * TODO : 리팩토링 해야할 것
 *  -> 바인딩 및 유효성 검증 : JSR-303 사용(DTO) 및 Validator 구현(@InitBinder) + HandlerMethodArgumentResolver
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class UserArticleController {

	private final ArticleService articleService;

    @Resource(name = "loginMemberBean")
    private Member loginMemberBean;

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
        if (id == null) return new ResultData("F-1", "id를 입력해주세요.");

		Article article = articleService.getForPrintArticle(id);

        if (article == null) return new ResultData("F-2", "존재하지 않는 게시물입니다.");

		return new ResultData("S-1", "조회 결과", "article", article);
	}

	/* 전체 게시물 조회 */
	@RequestMapping("/user/article/list")
	@ResponseBody
	public ResultData showList(@RequestParam(defaultValue = "1") int boardId,
                               @ModelAttribute Search search, @RequestParam(defaultValue = "1") int page) {
		log.info("UserArticleController.showList");
		log.info("search: {}", search);

        Board board = articleService.getBoard(boardId);
        if (board == null) {
            return new ResultData("F-1", "존재하지 않는 게시판 입니다.");
        }

        SearchKeywordType searchKeywordType = search.getSearchKeywordType();
        String searchKeyword = search.getSearchKeyword();

        // 한 페이지에 보여줄 게시물 개수
        int itemsInAPage = 20;

        List<Article> articles = articleService.getForPrintArticles(boardId, searchKeywordType, searchKeyword, page, itemsInAPage);
        return new ResultData("S-1", "조회 결과", "articles", articles);
	}

	@PostMapping(value = "/user/article/list", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResultData showListJson(@RequestParam(defaultValue = "1") int boardId,
                                   @RequestBody Search search, @RequestParam(defaultValue = "1") int page){
		log.debug("UserArticleController.showListJson");
		log.debug("search: {}", search);

        Board board = articleService.getBoard(boardId);
        if (board == null) {
            return new ResultData("F-1", "존재하지 않는 게시판 입니다.");
        }

        SearchKeywordType searchKeywordType = search.getSearchKeywordType();
        String searchKeyword = search.getSearchKeyword();

        // 한 페이지에 보여줄 게시물 개수
        int itemsInAPage = 20;

        List<Article> articles = articleService.getForPrintArticles(boardId, searchKeywordType, searchKeyword, page, itemsInAPage);
        return new ResultData("S-1", "조회 결과", "articles", articles);
	}

	/* 게시물 추가 */
	@RequestMapping("/user/article/doAdd")
	@ResponseBody
	public ResultData doAdd(Article article) {
		if (article.getTitle()== null) return new ResultData("F-1", "title을 입력해주세요.");
		if (article.getBody() == null) return new ResultData("F-1", "body을 입력해주세요.");

		return articleService.addArticle(article);
	}

	/* 게시물 삭제 */
	@RequestMapping("/user/article/doDelete")
	@ResponseBody
	public ResultData doDelete(Integer id) {
		if (id == null) return new ResultData("F-1", "id를 입력해주세요.");

		return articleService.deleteArticle(id);
	}

	/* 게시물 수정 */
	@RequestMapping("/user/article/doModify")
	@ResponseBody
	public ResultData doModify(Article article) {
		if (article.getId() == null) return new ResultData("F-1", "id를 입력해주세요.");
		if (article.getTitle() == null) return new ResultData("F-1", "title을 입력해주세요.");
		if (article.getBody() == null) return new ResultData("F-1", "body을 입력해주세요.");

        return articleService.modifyArticle(article);
	}

}