package com.tena.untact2021.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.tena.untact2021.dto.Article;

@Mapper
public interface ArticleDao {

	/* 게시물 조회 */
	Article findById(@Param("id") int id);

	/* TODO 전체 게시물 조회 */
	List<Article> findAll(@Param("searchKeywordType") String searchKeywordType, @Param("searchKeyword") String searchKeyword);

	/* 게시물 추가 */
	int save(@Param("title") String title, @Param("body") String body);

	/* 게시물 삭제 */
	boolean deleteById(@Param("id") int id);

	/* 게시물 수정 */
	void update(@Param("id") int id, @Param("title") String title, @Param("body") String body);

}
