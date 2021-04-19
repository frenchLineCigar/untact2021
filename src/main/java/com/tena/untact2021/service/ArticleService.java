package com.tena.untact2021.service;

import com.tena.untact2021.dao.ArticleDao;
import com.tena.untact2021.dto.Article;
import com.tena.untact2021.dto.Board;
import com.tena.untact2021.dto.File;
import com.tena.untact2021.dto.ResultData;
import com.tena.untact2021.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

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
	public ResultData addArticle(Article article, Map<String, MultipartFile> fileMap) {

		//게시물 저장
		articleDao.save(article);

		//생성된 게시물 번호
		int newArticleId = article.getId();

		//첨부 파일 정보 저장
		for (String fileInputName : fileMap.keySet()) {
			MultipartFile multipartFile = fileMap.get(fileInputName);

			String[] fileInputNameBits = fileInputName.split("__");
			// Ex) file__article__0__common__attachment__1
			// => ["file", "article", "0", "common", "attachment", "1"]
			// fileInputName : "file__{relTypeCode}__{relId}__{typeCode}__{type2Code}__{fileNo}"
			// fileInputNameBits : ["file", {relTypeCode}, {relId}, {typeCode}, {type2Code}, {fileNo}]

			// file 로 시작하지 않으면 skip
			if (fileInputNameBits[0].equals("file") == false) continue;

			// size 가 0 이하면 skip
			int fileSize = (int) multipartFile.getSize();
			if (fileSize <= 0) continue;

			// 파일 메타 정보 가공
			File file = File.builder()
					.relTypeCode(fileInputNameBits[1])
					.relId(newArticleId)
					.typeCode(fileInputNameBits[3])
					.type2Code(fileInputNameBits[4])
					.fileNo(Integer.parseInt(fileInputNameBits[5]))
					.fileSize(fileSize)
					.originFileName(multipartFile.getOriginalFilename())
					.fileExtTypeCode(Util.getFileExtTypeCodeFromFileName(multipartFile.getOriginalFilename()))
					.fileExtType2Code(Util.getFileExtType2CodeFromFileName(multipartFile.getOriginalFilename()))
					.fileExt(Util.getFileExtFromFileName(multipartFile.getOriginalFilename()))
					.fileDir(Util.getNowYearMonthDateStr())
					.build();

			// 메타 정보 저장
			fileService.saveFileMetaInfo(file);
		}

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

        return articleDao.findAllForPrint(boardId, searchKeywordType, searchKeyword, limitFrom, limitTake);
    }

    /* 게시판 정보 조회 */
    public Board getBoard(int boardId) {
        return articleDao.findBoard(boardId);
    }

}
