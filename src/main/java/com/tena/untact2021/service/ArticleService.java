package com.tena.untact2021.service;

import com.tena.untact2021.dao.ArticleDao;
import com.tena.untact2021.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.tena.untact2021.dto.Search.SearchKeywordType;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleService {

	private final ArticleDao articleDao;
	private final AttachFileService fileService;

	/* 전체 게시물 조회 */
	public List<Article> getArticles(SearchKeywordType searchKeywordType, String searchKeyword) {
		return articleDao.findAll(searchKeywordType, searchKeyword);
	}

	/* 게시물 조회 */
	public Article getArticle(int id) {
		return articleDao.findById(id);
	}

	/* 게시물 추가 */
	public ResultData addArticle(Article article) {

		//게시물 저장
		articleDao.save(article);

		//게시물의 첨부 파일 갱신
		changeInputFilesRelId(article);

		return new ResultData("S-1", "성공하였습니다.", "id", article.getId());
	}

	/* 게시물의 첨부 파일 갱신  */
	private void changeInputFilesRelId(Article article) {
		//첨부 파일이 있다면, 해당 파일의 게시물 번호(relId)를 갱신
		if (article.hasInputFiles()) {
			String fileIdsStr = article.getFileIdsStr(); // Ajax로 업로드한 파일 번호가 담긴 문자열
			Integer articleId = article.getId(); // 현재 게시물 번호
			fileService.changeRelIds(fileIdsStr, articleId);
		}
	}

	/* 게시물 삭제 */
	public ResultData deleteArticle(int id) {
		boolean result = articleDao.deleteById(id);

		// 게시물과 연관된 파일 삭제
		fileService.deleteFiles("article", id);

		if (result == false) {
			return new ResultData("F-2", "게시물 삭제에 실패했습니다.", "id", id);
		}

		return new ResultData("S-1", "게시물을 삭제하였습니다.", "id", id);
	}

	/* 게시물 수정 */
	public ResultData modifyArticle(Article article) {
        boolean result = articleDao.update(article);

        if (result == false) {
            return new ResultData("F-2", "게시물 수정에 실패했습니다.", "id", article.getId());
        }

        return new ResultData("S-1", "게시물을 수정하였습니다.", "id", article.getId());
	}

    /* 게시물 조회 (작성자명 포함) */
    public Article getForPrintArticle(int id) {
        return articleDao.findForPrintById(id);
    }

	/* 게시물 상세 조회 (작성자명, 게시판명, 썸네일 포함) */
	public Article getForDetailPrintById(int id) {
		ArticleDetail articleDetail = articleDao.findForDetailPrintById(id);

		Article article = Optional.ofNullable(articleDetail)
				.stream()
				.peek(ArticleDetail::validAndValuateArticle) //썸네일 있으면 이미지 링크 셋팅
				.map(ArticleDetail::getArticle)
				.findFirst().orElse(null); //결과 없을땐 그냥 null 리턴

		return article;
	}

    /* 전체 게시물 조회 */
    public List<Article> getForPrintArticles(int boardId, SearchKeywordType searchKeywordType, String searchKeyword,
                                             int page, int itemsInAPage) {
        int limitFrom = (page - 1) * itemsInAPage;
        int limitTake = itemsInAPage;

        // 게시물 가져오기
	    List<Article> articles = articleDao.findAllForPrint(boardId, searchKeywordType, searchKeyword, limitFrom, limitTake);

	    // 게시물 아이디 리스트
	    List<Integer> articleIds = articles.stream().map(article -> article.getId()).collect(Collectors.toList());

	    // 게시물 아이디 리스트에 해당하는 첨부파일 가져오기
	    List<AttachFile> filesByRelIds = fileService.getFilesByRelIds("article", articleIds, "common", "attachment");

	    // 게시물의 첨부파일 및 대표 이미지 설정
	    for (Article article : articles) {

		    // 해당 게시물의 첨부파일로 필터링 후, fileNo 를 키로 갖는 맵으로 가공
		    Map<Integer, AttachFile> fileMap = filesByRelIds.stream()
				    .filter(file -> file.getRelId() == article.getId())
				    .collect(Collectors.toMap(AttachFile::getFileNo, Function.identity()));

		    // 게시물에 첨부파일 정보 저장
		    article.addToExtra("fileMap", fileMap);

		    // 첨부 파일 중 이미지 타입이면서 순서가 빠른 파일을 썸네일로 사용
		    Optional<AttachFile> thumbFile = fileMap.values().stream()
				    .filter(attachFile -> attachFile.getFileExtTypeCode().equals("img")) // 이미지 타입 중
				    .min(Comparator.comparingInt(AttachFile::getFileNo)); //fileNo 가 빠른 것
		    thumbFile.
				    ifPresent(attachFile -> article.setExtra__thumbUrl(attachFile.getForPrintUrl()));
	    }

	    return articles;
    }

	/**
	 * Beta
	 *
	 * 전체 게시물 조회
	 */
	public List<Article> getForPrintArticlesV2(int boardId, SearchKeywordType searchKeywordType, String searchKeyword,
	                                           int page, int itemsInAPage) {
		int limitFrom = (page - 1) * itemsInAPage;
		int limitTake = itemsInAPage;

		// 게시물 가져오기
		List<Article> articles = articleDao.findAllForPrint(boardId, searchKeywordType, searchKeyword, limitFrom, limitTake);

		// 가져온 게시물들의 아이디만 리스트로 추출해서
		List<Integer> articleIds = articles.stream().map(Article::getId).collect(Collectors.toList());

		// articleIds 과 관련된 모든 첨부 파일을 맵 형태로 가져오기 : 상위키는 articleId, 하위키는 fileNo
		Map<Integer, Map<Integer, AttachFile>> filesMap = fileService.getFilesMapKeyRelIdAndFileNo("article", articleIds, "common", "attachment");

		// 게시물 당 첨부파일 및 대표 이미지 설정
		for (Article article : articles) {
			// 해당 게시물의 첨부파일 추출
			Map<Integer, AttachFile> fileMap = filesMap.get(article.getId());

			if (fileMap != null) {
				// 게시물과 연관된 첨부파일 셋팅
				article.addToExtra("fileMap", fileMap);

				// 첨부 파일 중 이미지 타입이면서 순서가 빠른 파일을 썸네일로 사용
				Optional<AttachFile> thumbFile = fileMap.values()
						.stream()
						.filter(attachFile -> attachFile.getFileExtTypeCode().equals("img")) // 이미지 타입 중
						.min(Comparator.comparingInt(AttachFile::getFileNo)); //fileNo 가 빠른 것
				thumbFile.
						ifPresent(attachFile -> article.setExtra__thumbUrl(attachFile.getForPrintUrl())); // 해당하는 파일이 있으면 URL 을 썸네일 링크로 설정
			}
		}

		return articles;
	}

    /* 게시판 정보 조회 */
    public Board getBoard(int boardId) {
        return articleDao.findBoard(boardId);
    }

    /* 전체 게시물 개수 조회 */
	public int getTotalCount(int boardId, SearchKeywordType searchKeywordType, String searchKeyword) {
		return articleDao.getTotalCount(boardId, searchKeywordType, searchKeyword);
	}
}
