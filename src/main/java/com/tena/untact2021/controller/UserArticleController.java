package com.tena.untact2021.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tena.untact2021.dto.Article;
import com.tena.untact2021.util.Util;

@Controller
public class UserArticleController {
	private int articlesLastId;
	private List<Article> articles;

	public UserArticleController() {
		// 멤버변수 초기화
		articlesLastId = 0;
		articles = new ArrayList<>();

		// mock 게시물 2개 생성
		articles.add(new Article(++articlesLastId, "2020-12-12 12:12:12", "2020-12-12 12:12:12", "제목1", "내용1"));
		articles.add(new Article(++articlesLastId, "2020-12-12 12:12:12", "2020-12-12 12:12:12", "제목2", "내용2"));
	}

	@RequestMapping("/user/article/detail")
	@ResponseBody
	public Article showDetail(int id) {
		return articles.get(id - 1);
	}

	@RequestMapping("/user/article/list")
	@ResponseBody
	public List<Article> showList() {
		return articles;
	}
	
	@RequestMapping("/user/article/doAdd")
	@ResponseBody
	public Map<String, Object> doAdd(String title, String body) {
		
		String regDate = Util.getNowDateStr();
		String updateDate = regDate; //처음 생성시점에는 작성날짜와 수정날짜가 동일
		
		articles.add(new Article(++articlesLastId, regDate, updateDate, title, body));
//		int id = (articles.get(articles.size() - 1).getId()) + 1;
//		articles.add(new Article(id, regDate, title, body));
		
		Map<String, Object> rs = new HashMap<>();
		rs.put("resultCode", "S-1");
		rs.put("msg", "성공하였습니다.");
		rs.put("id", articlesLastId);
//		rs.put("id", id);
		
		return rs;
	}

	@RequestMapping("/user/article/doDelete")
	@ResponseBody
	public Map<String, Object> doDelete(int id) {
		boolean deleteArticleRs = deleteArticle(id);
		
		Map<String, Object> rs = new HashMap<>();
		
		if (deleteArticleRs) {
			rs.put("resultCode", "S-1");
			rs.put("msg", "성공하였습니다.");
		} else {
			rs.put("resultCode", "F-1");
			rs.put("msg", "해당 게시물은 이미 삭제되었거나 존재하지 않습니다.");
		}
		
		rs.put("id", id);
		
		return rs;
	}

	private boolean deleteArticle(int id) {
		for (Article article : articles) {
			if (article.getId() == id) {
				articles.remove(article);
				return true;
			}
		}
		
		return false;
	}
	
	@RequestMapping("/user/article/doModify")
	@ResponseBody
	public Map<String, Object> doModify(int id, String title, String body) {
		Article selectedArticle = null;
		
		// 해당 게시물 유무 조회
		for (Article article : articles) {
			if (article.getId() == id) {
				selectedArticle = article;
				break;
			}
		}
		
		Map<String, Object> rs = new HashMap<>();
		
		if (selectedArticle == null) {
			rs.put("resultCode", "F-1");
			rs.put("msg", String.format("%d번 게시물은 존재하지 않습니다.", id));
			return rs;
		}
		
		// 해당 게시물 수정
		selectedArticle.setUpdateDate(Util.getNowDateStr());
		selectedArticle.setTitle(title);
		selectedArticle.setBody(body);
		
		rs.put("resultCode", "S-1");
		rs.put("msg", String.format("%d번 게시물은 수정되었습니다.", id));
		rs.put("id", id);
		
		return rs;
	}

}