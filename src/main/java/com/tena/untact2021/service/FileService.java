package com.tena.untact2021.service;

import com.google.common.base.Joiner;
import com.tena.untact2021.dao.FileDao;
import com.tena.untact2021.dto.AttachFile;
import com.tena.untact2021.dto.ResultData;
import com.tena.untact2021.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class FileService {

	private final FileDao fileDao;
	@Value("${custom.fileDirPath}")
	private String fileDirPath;

	/* 메타 데이터 저장 (DB)*/
	public void saveFileMetaData(AttachFile attachFile) {
		fileDao.save(attachFile);
	}

	/* 파일 저장 */
	public ResultData save(MultipartFile multipartFile, int relId) {
		String fileInputName = multipartFile.getName();
		String[] fileInputNameBits = fileInputName.split("__");
		int fileSize = (int) multipartFile.getSize();

		// 파라미터명 prefix 체크 -> file 이 아니면 ㄴㄴ
		if (fileInputNameBits[0].equals("file") == false) return new ResultData("F-1", "파라미터 명이 올바르지 않습니다.");

		// 크기 체크 ->  0 이하면 ㄴㄴ
		if (fileSize <= 0) return new ResultData("F-2", "파일이 업로드 되지 않았습니다.");

		AttachFile attachFile = AttachFile.from(multipartFile, relId);

		// 파일 메타 데이터 DB에 저장
		saveFileMetaData(attachFile);

		// 실제 파일 저장
		return saveRealFileOnDisk(multipartFile, attachFile);
	}

	/* 실제 파일 저장 (서버 경로) */
	public ResultData saveRealFileOnDisk(MultipartFile multipartFile, AttachFile attachFile) {
		int newFileId = attachFile.getId();
		String relTypeCode = attachFile.getRelTypeCode();
		String fileDir = attachFile.getFileDir();
		String fileExt = attachFile.getFileExt();

		// 새 파일이 저장될 폴더 객체(java.io.File) 생성
		String targetDirPath = fileDirPath + "/" + relTypeCode + "/" + fileDir;
		File targetDir = new File(targetDirPath);

		// 새 파일이 저장될 폴더가 존재하지 않는다면 생성
		if (!targetDir.exists()) targetDir.mkdirs();

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
	public AttachFile getThumbnail(String relTypeCode, int relId, String typeCode, String type2Code) {
		return fileDao.findThumbnail(relTypeCode, relId, typeCode, type2Code);
	}

	/* 단일 파일 1개 (FileNo 로 특정) */
	public AttachFile getOneByFileNo(String relTypeCode, int relId, String typeCode, String type2Code, int fileNo) {
		return fileDao.findOneByFileNo(relTypeCode, relId, typeCode, type2Code, fileNo);
	}

	/* 파일 조회 */
	public List<AttachFile> getFiles(String relTypeCode, int relId, String typeCode, String type2Code) {
		return fileDao.findFiles(relTypeCode, relId, typeCode, type2Code);
	}

	private List<AttachFile> getFiles(String relTypeCode, int relId) {
		return fileDao.findFiles(relTypeCode, relId, null, null);
	}

	/* Ajax 업로드 파일 저장 처리 */
	public ResultData saveFiles(MultipartRequest multipartRequest) {
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();

		Map<String, ResultData> filesResultData = new LinkedHashMap<>();
		List<Integer> fileIds = new ArrayList<>();

		for (String fileInputName : fileMap.keySet()) {
			MultipartFile multipartFile = fileMap.get(fileInputName);

			if (! multipartFile.isEmpty()) {
				ResultData fileResultData = save(multipartFile, 0); // 연관 게시물 생성 전이므로 relId를 0으로 처리
				int fileId = (int) fileResultData.getBody().get("id");

				fileIds.add(fileId);

				filesResultData.put(fileInputName, fileResultData);
			}
		}

		String fileIdsStr = Joiner.on(",").join(fileIds);
		// String fileIdsStr = StringUtils.join(fileIds, ",")
		// String fileIdsStr = fileIds.stream().map(String::valueOf).collect(Collectors.joining(","));

		return new ResultData("S-1", " 파일을 업로드하였습니다.", "filesResultData", filesResultData, "fileIdsStr", fileIdsStr);
	}

	/* 파일과 연관된 게시물 번호(relId) 변경 */
	public void changeRelIdInFiles(String fileIdsStr, int relId) {
		List<Integer> fileIds = Util.getIdsToList(fileIdsStr, ",");
		fileIds.forEach(fileId -> changeRelId(fileId, relId));
		/*
		Arrays.stream(fileIdsStr.split(","))
				.map(id -> Integer.parseInt(id.trim()))
				.forEach(fileId -> changeRelId(fileId, relId));
		*/
	}

	public void changeRelId(int id, int relId) {
		fileDao.updateRelId(id, relId);
	}

	/* 파일 삭제 */
	public void deleteFiles(String relTypeCode, int relId) {
		List<AttachFile> files = getFiles(relTypeCode, relId);

		for (AttachFile file : files) {
			deleteFile(file);
		}
	}

	private void deleteFile(AttachFile file) {
		// 실제 파일 삭제
		String filePath = file.getFilePath(fileDirPath);
		Util.deleteFile(filePath);

		// 파일 정보 삭제
		fileDao.deleteFile(file.getId());
	}
}
