package com.tena.untact2021.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.tena.untact2021.dto.Article;

import static com.tena.untact2021.dto.Search.*;

@Mapper
public interface ArticleDao {

	/* 게시물 조회 */
	Article findById(@Param("id") int id);

	/* 전체 게시물 조회 */
	List<Article> findAll(@Param("searchKeywordType") SearchKeywordType searchKeywordType, @Param("searchKeyword") String searchKeyword);

	/* 게시물 추가 */
	void save(Article article);

	/* 게시물 삭제 */
	boolean deleteById(@Param("id") int id);

	/* 게시물 수정 */
    boolean update(Article article);

    /* 게시물 조회 (작성자 정보 포함) */
    Article findForPrintById(@Param("id") int id);

    /* 전체 게시물 조회 (작성자 정보 포함) */
    List<Article> findAllForPrint(@Param("searchKeywordType") SearchKeywordType searchKeywordType,
                                  @Param("searchKeyword") String searchKeyword);

}
