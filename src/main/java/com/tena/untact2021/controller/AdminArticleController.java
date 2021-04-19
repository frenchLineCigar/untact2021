package com.tena.untact2021.controller;

import com.tena.untact2021.custom.CurrentMember;
import com.tena.untact2021.dto.*;
import com.tena.untact2021.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.util.List;
import java.util.Map;

import static com.tena.untact2021.dto.Search.SearchKeywordType;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AdminArticleController extends BaseController {

	private final ArticleService articleService;

	/* 게시물 조회 */
	@RequestMapping("/admin/article/detail")
	@ResponseBody
	public ResultData showDetail(Integer id) {
		if (id == null) return new ResultData("F-1", "id를 입력해주세요.");

		Article article = articleService.getForPrintArticle(id);

		if (article == null) return new ResultData("F-2", "존재하지 않는 게시물입니다.");

		return new ResultData("S-1", "조회 결과", "article", article);
	}

	/* 전체 게시물 조회 */
	@RequestMapping("/admin/article/list")
	public String showList(@RequestParam(defaultValue = "1") int boardId, @RequestParam(defaultValue = "1") int page,
	                       @ModelAttribute Search search, Model model) {

		Board board = articleService.getBoard(boardId);
		if (board == null) {
			return msgAndBack(model, "존재하지 않는 게시판 입니다.");
		}

		SearchKeywordType searchKeywordType = search.getSearchKeywordType();
		String searchKeyword = search.getSearchKeyword();

		// 한 페이지에 보여줄 게시물 개수
		int itemsInAPage = 20;

		List<Article> articles = articleService.getForPrintArticles(boardId, searchKeywordType, searchKeyword, page, itemsInAPage);
		model.addAttribute("articles", articles);

		return "admin/article/list";
	}

	/* 게시물 추가 폼 */
	@RequestMapping("/admin/article/add")
	public String showAdd(Article article, @CurrentMember Member currentMember) {
		return "admin/article/add";
	}

	/* 게시물 추가 */
	@RequestMapping("/admin/article/doAdd")
	@ResponseBody
	public ResultData doAdd(Article article, @CurrentMember Member currentMember, MultipartRequest multipartRequest) {
		if (article.getBoardId() == null) return new ResultData("F-1", "boardId를 입력해주세요.");
		if (article.getTitle() == null) return new ResultData("F-1", "title을 입력해주세요.");
		if (article.getBody() == null) return new ResultData("F-1", "body을 입력해주세요.");

		//작성자 정보는 현재 인증된 사용자
		article.setMemberId(currentMember.getId());

		//첨부파일 정보가 담긴 Map
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();

		//게시물 저장
		return articleService.addArticle(article, fileMap);
	}

	/* 게시물 삭제 */
	@RequestMapping("/admin/article/doDelete")
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
	@RequestMapping("/admin/article/doModify")
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