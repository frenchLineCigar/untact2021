package com.tena.untact2021.service;

import com.tena.untact2021.dao.ArticleDao;
import com.tena.untact2021.dto.Article;
import com.tena.untact2021.dto.AttachFile;
import com.tena.untact2021.dto.Board;
import com.tena.untact2021.dto.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.tena.untact2021.dto.Search.SearchKeywordType;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleService {

	private final ArticleDao articleDao;
	private final FileService fileService;

	/* 전체 게시물 조회 */
	public List<Article> getArticles(SearchKeywordType searchKeywordType, String searchKeyword) {
		return articleDao.findAll(searchKeywordType, searchKeyword);
	}

	/* 게시물 조회 */
	public Article getArticle(int id) {
		return articleDao.findById(id);
	}

	/* 게시물 추가 -> TODO : User 쪽 코드도 완료되면 제거 */
	public ResultData addArticle(Article article) {
        articleDao.save(article);

		return new ResultData("S-1", "성공하였습니다.", "id", article.getId());
	}

	/* 게시물 추가 (+ 첨부 파일 ) */
	public ResultData addArticle(Article article, List<AttachFile> attachFiles) {

		//게시물 저장
		articleDao.save(article);

		//생성된 게시물 번호
		int newArticleId = article.getId();

		//파일 저장 처리
		fileService.save(attachFiles, newArticleId);

		return new ResultData("S-1", "성공하였습니다.", "id", article.getId());
	}

	/* 게시물 삭제 */
	public ResultData deleteArticle(int id) {
		boolean result = articleDao.deleteById(id);

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

    /* 게시물 조회 (작성자 정보 포함) */
    public Article getForPrintArticle(int id) {
        return articleDao.findForPrintById(id);
    }

    /* 전체 게시물 조회 (작성자 정보 포함) */
    public List<Article> getForPrintArticles(int boardId, SearchKeywordType searchKeywordType, String searchKeyword,
                                             int page, int itemsInAPage) {
        int limitFrom = (page - 1) * itemsInAPage;
        int limitTake = itemsInAPage;

	    List<Article> articles = articleDao.findAllForPrint(boardId, searchKeywordType, searchKeyword, limitFrom, limitTake);

	    // 첨부파일 1이 이미지면 썸네일로 사용
	    for (Article article : articles) {
		    AttachFile file = fileService.getFileByThumbnailCondition("article", article.getId(), "common", "attachment", 1);
		    if (file != null) {
			    article.setExtra__thumbImg(file.getForPrintUrl());
			    System.out.println("article.getExtra__thumbImg() = " + article.getExtra__thumbImg());
		    }
	    }

	    return articles;
    }

    /* 게시판 정보 조회 */
    public Board getBoard(int boardId) {
        return articleDao.findBoard(boardId);
    }

}
