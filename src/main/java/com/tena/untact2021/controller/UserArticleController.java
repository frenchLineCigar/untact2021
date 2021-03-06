package com.tena.untact2021.controller;

import com.tena.untact2021.custom.CurrentMember;
import com.tena.untact2021.dto.*;
import com.tena.untact2021.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.tena.untact2021.dto.Search.SearchKeywordType;

/**
 * TODO : 리팩토링 해야할 것
 *  -> 바인딩 및 유효성 검증 : JSR-303 사용(DTO) 및 Validator 구현(@InitBinder) + HandlerMethodArgumentResolver
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class UserArticleController {

	private final ArticleService articleService;

    /* 게시물 조회 */
	@GetMapping("/user/article/detail")
	@ResponseBody
	public ResultData showDetail(Integer id) {
        if (id == null) return new ResultData("F-1", "id를 입력해주세요.");

		Article article = articleService.getForPrintArticle(id);

        if (article == null) return new ResultData("F-2", "존재하지 않는 게시물입니다.");

		return new ResultData("S-1", "조회 결과", "article", article);
	}

	/* 전체 게시물 조회 */
	@GetMapping("/user/article/list")
	@ResponseBody
	public ResultData showList(@RequestParam(defaultValue = "1") int boardId,
                               @RequestParam(defaultValue = "1") int page,
                               @ModelAttribute Search search) {
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

	@PostMapping(value = "/user/article/list", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResultData showListJson(@RequestParam(defaultValue = "1") int boardId,
                                   @RequestParam(defaultValue = "1") int page,
                                   @RequestBody Search search){
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
	@PostMapping("/user/article/doAdd")
	@ResponseBody
	public ResultData doAdd(Article article, @CurrentMember Member currentMember) {
		if (article.getBoardId() == null) return new ResultData("F-1", "boardId를 입력해주세요.");
		if (article.getTitle() == null) return new ResultData("F-1", "title을 입력해주세요.");
		if (article.getBody() == null) return new ResultData("F-1", "body을 입력해주세요.");

        //작성자 정보는 현재 인증된 사용자
        article.setMemberId(currentMember.getId());

		return articleService.addArticle(article);
	}

	/* 게시물 삭제 */
	@GetMapping("/user/article/doDelete")
	@ResponseBody
	public ResultData doDelete(Integer id) {
		if (id == null) return new ResultData("F-1", "id를 입력해주세요.");

        //게시물 유무 확인
        Article existingArticle = articleService.getArticle(id);
        if (existingArticle == null) {
            return new ResultData("F-1", "해당 게시물은 존재하지 않습니다.");
        }

		return articleService.deleteArticle(id);
	}

	/* 게시물 수정 */
	@PostMapping("/user/article/doModify")
	@ResponseBody
	public ResultData doModify(Article article) {
		if (article.getId() == null) return new ResultData("F-1", "id를 입력해주세요.");
		if (article.getTitle() == null) return new ResultData("F-1", "title을 입력해주세요.");
		if (article.getBody() == null) return new ResultData("F-1", "body을 입력해주세요.");

        //게시물 유무 확인
        Article existingArticle = articleService.getArticle(article.getId());
        if (existingArticle == null) {
            return new ResultData("F-1", "해당 게시물은 존재하지 않습니다.");
        }

        return articleService.modifyArticle(article);
	}

}