package com.tena.untact2021.service;

import com.tena.untact2021.dao.ArticleDao;
import com.tena.untact2021.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

	/* 게시물 추가 */
	public ResultData addArticle(Article article) {

		//게시물 저장
		articleDao.save(article);

		//생성된 게시물 번호
		int articleId = article.getId();

		//업로드된 파일 번호
		String fileIdsStr = article.getFileIdsStr();

		//업로드된 파일의 연관 게시물 번호(relId) 수정
		if (! fileIdsStr.isBlank()) {
			// "1,2" -> [1, 2]
			List<Integer> fileIds = Arrays.stream(fileIdsStr.split(","))
										.map(s -> Integer.valueOf(s.trim()))
										.collect(Collectors.toList());

			// 처음에 0으로 할당해서 업로드한 첨부파일들의 relId를 생성된 게시물 번호로 수정한다
			for (int fileId : fileIds) {
				fileService.changeRelId(fileId, articleId);
			}
		}

		return new ResultData("S-1", "성공하였습니다.", "id", article.getId());
	}

//	/* 게시물 추가  */
//	public ResultData addArticle(Article article, Map<String, MultipartFile> fileMap) {
//
//		//게시물 저장
//		articleDao.save(article);
//
//		//생성된 게시물 번호
//		int newArticleId = article.getId();
//
//		//파일 저장 처리
//		fileMap.keySet().stream()
//				.map(fileMap::get)
//				.filter(not(MultipartFile::isEmpty)) // no file, no content면 skip
//				.forEach(multipartFile -> fileService.save(multipartFile, newArticleId));
//
//		return new ResultData("S-1", "성공하였습니다.", "id", article.getId());
//	}

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

    /* 게시물 조회 (작성자명 포함) */
    public Article getForPrintArticle(int id) {
        return articleDao.findForPrintById(id);
    }

	/* 게시물 상세 조회 (작성자명, 게시판명, 썸네일 포함) */
	public Article getForDetailPrintById(int id) {
		ArticleDetail articleDetail = articleDao.findForDetailPrintById(id);
		System.out.println("articleDetail = " + articleDetail);

		Article article = articleDetail.getArticle();
		AttachFile thumbImgFile = articleDetail.getThumbImgFile();
		if (thumbImgFile != null) {
			article.setExtra__thumbImg(thumbImgFile.getForPrintUrl());
		}

		return article;
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
		    }
	    }

	    return articles;
    }

    /* 게시판 정보 조회 */
    public Board getBoard(int boardId) {
        return articleDao.findBoard(boardId);
    }

}
