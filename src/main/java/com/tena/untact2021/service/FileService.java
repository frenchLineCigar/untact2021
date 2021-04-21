package com.tena.untact2021.service;

import com.tena.untact2021.dao.FileDao;
import com.tena.untact2021.dto.AttachFile;
import com.tena.untact2021.dto.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class FileService {

	private final FileDao fileDao;
	@Value("${custom.fileDirPath}")
	private String fileDirPath;

	/* 파일 저장 */
	public ResultData save(List<AttachFile> attachFiles, int relId) {
		List<Integer> newFileIds = new ArrayList<>();
		attachFiles.stream().filter(AttachFile::hasData).forEach(attachFile -> {
			attachFile.setRelId(relId); // 첨부 파일에 게시물 번호 셋팅
			saveFileMetaData(attachFile); // 파일 메타 데이터 DB에 저장
			saveRealFileOnDisk(attachFile);// 실제 파일 저장
			newFileIds.add(attachFile.getId()); //저장된 파일 번호 담기
		});

		return new ResultData("S-1", "저장된 파일 번호", "newFileIds", newFileIds);
	}

	/* 메타 데이터 저장 (DB)*/
	public ResultData saveFileMetaData(AttachFile attachFile) {
		fileDao.save(attachFile);

		return new ResultData("S-1", "성공하였습니다.", "id", attachFile.getId());
	}

	/* 실제 파일 저장 (서버 경로) */
	public ResultData saveRealFileOnDisk(AttachFile attachFile) {
		int newFileId = attachFile.getId();
		String relTypeCode = attachFile.getRelTypeCode();
		String fileDir = attachFile.getFileDir();
		String fileExt = attachFile.getFileExt();
		MultipartFile multipartFile = attachFile.getMultipartFile();

		// 새 파일이 저장될 폴더 객체(java.io.File) 생성
		String targetDirPath = fileDirPath + "/" + relTypeCode + "/" + fileDir;
		File targetDir = new File(targetDirPath);

		// 새 파일이 저장될 폴더가 존재하지 않는다면 생성
		if (targetDir.exists() == false) {
			targetDir.mkdirs();
		}

		// Ex. ${fileDirPath}/article/2021_03/1.jpg
		// 새 파일 이름 지정
		String targetFileName = newFileId + "." + fileExt;
		// 새 파일 경로 지정
		String targetFilePath = targetDirPath + "/" + targetFileName;

		// 파일 생성(업로드된 파일을 지정된 경로로 옮김)
		try {
			multipartFile.transferTo(new File(targetFilePath));
		} catch (IllegalStateException | IOException e) {
			//e.printStackTrace();
			return new ResultData("F-1", "파일 저장에 실패하였습니다.");
		}

		Map<String, Object> resultMap = new LinkedHashMap<>();
		resultMap.put("id", newFileId);
		resultMap.put("fileRealPath", targetFilePath);
		resultMap.put("fileName", targetFileName);

		return new ResultData("S-1", "파일이 저장되었습니다.", resultMap);
	}

	/* 썸네일 가져오기 */
	public AttachFile getFileByThumbnailCondition(String relTypeCode, int relId, String typeCode, String type2Code, int fileNo) {
		return fileDao.findFileByThumbnailCondition(relTypeCode, relId, typeCode, type2Code, fileNo);
	}
}
