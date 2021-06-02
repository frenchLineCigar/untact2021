package com.tena.untact2021.controller;

import com.tena.untact2021.custom.CurrentMember;
import com.tena.untact2021.dto.*;
import com.tena.untact2021.service.ArticleService;
import com.tena.untact2021.service.AttachFileService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.tena.untact2021.dto.Search.SearchKeywordType;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AdminArticleController extends BaseController {

	private final ArticleService articleService;
	private final AttachFileService fileService;

	/* 게시물 상세 조회 */
	@GetMapping("/admin/article/detail")
	@ApiOperation(value = "게시물 상세", notes = "성공시 게시물에 대한 상세정보를 반환합니다.")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value ="게시물 번호", example = "1", required = true)
	})
	@ApiResponses({
			@ApiResponse(code = 200, message = "성공"),
			@ApiResponse(code = 400, message = "잘못된 접근"),
			@ApiResponse(code = 500, message = "서버 에러")
	})
	public String showDetail(Integer id, Model model) {
		if (id == null) return msgAndBack("id를 입력해주세요.");

		//Article article = articleService.getForPrintArticle(id);
		Article article = articleService.getForDetailPrintById(id);

		if (article == null) return msgAndBack(model, "존재하지 않는 게시물번호 입니다.");

		// 해당 게시물의 첨부파일 리스트 가져오기
		List<AttachFile> files = fileService.getFiles("article", article.getId(), "common", "attachment");

		// 파일 리스트를 fileNo 를 키로 갖는 맵으로 가공
		Map<Integer, AttachFile> fileMap = files.stream().collect(Collectors.toMap(AttachFile::getFileNo, Function.identity()));

		// 게시물에 첨부파일 정보 담기
		article.addToExtra("fileMap", fileMap);

		model.addAttribute("article", article);

		return "admin/article/detail";
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

		int totalItemCount = articleService.getTotalCount(boardId, searchKeywordType, searchKeyword); // 총 게시물 개수
		int itemsInAPage = 10; // 한 페이지에 보여줄 게시물 개수
		int totalPage = (int) Math.ceil(totalItemCount / (double) itemsInAPage); // 전체 페이지 계산
		int pageMenuArmSize = 10; // 현재 페이지 중심으로 좌, 우에 각각 표시할 페이지 수

		int pageMenuStart = page - pageMenuArmSize;
		if (pageMenuStart < 1) {
			pageMenuStart = 1;
		}

		int pageMenuEnd = page + pageMenuArmSize;
		if (pageMenuEnd > totalPage) {
			pageMenuEnd = totalPage;
		}

		// List<Article> articles = articleService.getForPrintArticles(boardId, searchKeywordType, searchKeyword, page, itemsInAPage);
		List<Article> articles = articleService.getForPrintArticlesV2(boardId, searchKeywordType, searchKeyword, page, itemsInAPage); // Beta

		model.addAttribute("board", board);
		model.addAttribute("articles", articles);
		model.addAttribute("totalItemCount", totalItemCount);
		model.addAttribute("totalPage", totalPage);
		model.addAttribute("page", page);
		model.addAttribute("pageMenuArmSize", pageMenuArmSize);
		model.addAttribute("pageMenuStart", pageMenuStart);
		model.addAttribute("pageMenuEnd", pageMenuEnd);

		return "admin/article/list";
	}

	/* 게시물 추가 폼 */
	@RequestMapping("/admin/article/add")
	public String showAdd(Article article, @CurrentMember Member currentMember) {
		return "admin/article/add";
	}

	/* 게시물 추가 */
	@RequestMapping("/admin/article/doAdd")
	public String doAdd(Article article, @CurrentMember Member currentMember, Model model) {
		if (article.getBoardId() == null) return msgAndBack(model, "boardId를 입력해주세요.");
		if (article.getTitle() == null) return msgAndBack(model, "title을 입력해주세요.");
		if (article.getBody() == null) return msgAndBack(model, "body을 입력해주세요.");

		//작성자 정보는 현재 인증된 사용자
		article.setMemberId(currentMember.getId());

		//게시물 저장
		ResultData addArticleRd = articleService.addArticle(article);
		int newArticleId = (int) addArticleRd.getBody().get("id");

		return msgAndReplace(model, String.format("%d번 게시물이 작성되었습니다.", newArticleId), "./detail?id=" + newArticleId);
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

	/* 게시물 수정 폼 */
	@RequestMapping("/admin/article/modify")
	public String showModify(Integer id, Model model) {
		if (id == null) return msgAndBack(model, "id를 입력해주세요.");

		// 해당 게시물 가져오기
		Article article = articleService.getForPrintArticle(id);

		if (article == null) return msgAndBack(model, "존재하지 않는 게시물번호 입니다.");

		// 해당 게시물의 첨부파일 리스트 가져오기
		List<AttachFile> files = fileService.getFiles("article", article.getId(), "common", "attachment");

		// 파일 리스트를 fileNo 를 키로 갖는 맵으로 가공
		Map<Integer, AttachFile> fileMap = files.stream().collect(Collectors.toMap(AttachFile::getFileNo, Function.identity()));

		// 게시물에 첨부파일 정보 담기
		article.addToExtra("fileMap", fileMap);

		model.addAttribute("article", article);

		return "admin/article/modify";
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