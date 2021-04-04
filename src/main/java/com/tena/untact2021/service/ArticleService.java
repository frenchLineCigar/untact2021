package com.tena.untact2021.service;

import java.util.List;

import com.tena.untact2021.dto.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tena.untact2021.dao.ArticleDao;
import com.tena.untact2021.dto.Article;
import com.tena.untact2021.dto.ResultData;

import lombok.RequiredArgsConstructor;

import javax.annotation.Resource;

import static com.tena.untact2021.dto.Search.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleService {

	private final ArticleDao articleDao;

    @Resource(name = "loggedInMember")
    private Member loggedInMember;

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

	    //작성자 정보는 현재 세션에 로그인한 사용자
        article.setMemberId(loggedInMember.getId());

        articleDao.save(article);

		return new ResultData("S-1", "성공하였습니다.", "id", article.getId());
	}

	/* 게시물 삭제 */
	public ResultData deleteArticle(int id) {

	    //게시물 유무 확인
        Article existingArticle = articleDao.findById(id);
        if (existingArticle == null) {
            return new ResultData("F-1", "해당 게시물은 존재하지 않습니다.");
        }
        
        //삭제 권한 체크
        if (! loggedInMember.canDelete(existingArticle)) {
            return new ResultData("F-1", " 권한이 없습니다.");
        }

		boolean result = articleDao.deleteById(existingArticle.getId());

		if (result == false) {
			return new ResultData("F-2", "해당 게시물은 존재하지 않습니다.", "id", existingArticle.getId());
		}

		return new ResultData("S-1", "삭제하였습니다.", "id", existingArticle.getId());
	}

	/* 게시물 수정 */
	public ResultData modifyArticle(Article article) {

	    //게시물 유무 확인
        Article existingArticle = articleDao.findById(article.getId());
        if (existingArticle == null) {
            return new ResultData("F-1", "해당 게시물은 존재하지 않습니다.");
        }

        //수정 권한 체크
        if (! loggedInMember.canModify(existingArticle)) {
            return new ResultData("F-1", " 권한이 없습니다.");
        }

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
    public List<Article> getForPrintArticle(SearchKeywordType searchKeywordType, String searchKeyword) {
        return articleDao.findAllForPrint(searchKeywordType, searchKeyword);
    }
}
