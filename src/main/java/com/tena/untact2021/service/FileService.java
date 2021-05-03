package com.tena.untact2021.service;

import com.google.common.base.Joiner;
import com.tena.untact2021.dao.FileDao;
import com.tena.untact2021.dto.AttachFile;
import com.tena.untact2021.dto.ResultData;
import com.tena.untact2021.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class) // 모든 예외에 대해서 트랜잭션 롤백, 파일 저장 시 IOException(Checked Exception) 터지면 롤백 처리
@RequiredArgsConstructor
public class FileService {

	private final FileDao fileDao;

	@Value("${custom.fileDirPath}")
	private String fileDirPath;

	/* id로 파일 조회 */
	public AttachFile getFileById(int id) {
		return fileDao.findById(id);
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

	/* 파일의 연관 게시물 번호(relId) 변경 */
	public void changeRelId(int id, int relId) {
		fileDao.updateRelId(id, relId);
	}

	public void changeRelIds(List<Integer> ids, int relId) {
		for (Integer id : ids) {
			changeRelId(id, relId);
		}
	}

	public void changeRelIds(String idsStr, int relId) {
		List<Integer> ids = Util.getIdsToList(idsStr, ",");
		changeRelIds(ids, relId);
	}

	/* 파일 삭제 */
	private void deleteFile(AttachFile file) {
		// 실제 파일 삭제
		String filePath = file.getFilePath(fileDirPath);
		Util.deleteFile(filePath);

		// 파일 정보 삭제
		fileDao.deleteFile(file.getId());
	}

	public void deleteFiles(String relTypeCode, int relId) {
		List<AttachFile> files = getFiles(relTypeCode, relId);

		for (AttachFile file : files) {
			deleteFile(file);
		}
	}

	/* 파일 메타 데이터 저장 (DB) */
	public void saveFileMetaData(AttachFile attachFile) {
		fileDao.save(attachFile);
	}

	/* 실제 파일 저장 (서버 경로) */
	public boolean saveRealFileOnDisk(MultipartFile multipartFile, String targetFilePath) {
		Path path = Path.of(targetFilePath).toAbsolutePath().normalize();

		// 업로드된 파일을 지정된 경로로 옮김
		try {
			multipartFile.transferTo(path);
			// throw new IOException("Force Checked Exception");
		} catch (IllegalStateException | IOException e) {
			// log.info("Exception Caught");
			// e.printStackTrace();
			return false;
		}

		return true;
	}

	/* 파일 저장 */
	public ResultData saveFile(MultipartFile multipartFile) {

		String paramName = multipartFile.getName();
		String[] paramNameBits = paramName.split("__");

		// 파라미터명 체크 -> file 이 아니면 ㄴㄴ
		boolean isValidParam = paramNameBits[0].equals("file");
		if (!isValidParam) return new ResultData("F-1", "파라미터 명이 올바르지 않습니다.");

		// 크기 체크 ->  0 이하면 ㄴㄴ
		int fileSize = (int) multipartFile.getSize();
		if (fileSize <= 0) return new ResultData("F-2", "파일이 업로드 되지 않았습니다.");

		// AttachFile 도메인 타입으로 파일 메타 데이터 맵핑
		AttachFile file = AttachFile.from(multipartFile);

		// 파일 변경 시 기존 위치의 파일 삭제
		checkAndDeleteOldFileIfExists(file);

		// 파일 메타 데이터 저장 (DB)
		saveFileMetaData(file);

		// 파일이 저장될 폴더 객체(java.io.File) 생성
		String targetDirPath = fileDirPath + "/" + file.getRelTypeCode() + "/" + file.getFileDir();
		File targetDir = new File(targetDirPath);

		// 파일이 저장될 폴더가 존재하지 않는다면 생성
		if (!targetDir.exists()) targetDir.mkdirs();

		// 파일 이름
		String targetFileName = file.getId() + "." + file.getFileExt();

		// 파일 저장 경로
		String targetFilePath = targetDirPath + "/" + targetFileName; // Ex. ${fileDirPath}/article/2021_03/1.jpg

		// 실제 파일 저장 (서버 경로)
		boolean isSuccess = saveRealFileOnDisk(multipartFile, targetFilePath);

		if (! isSuccess) {
			return new ResultData("F-1", "파일 저장에 실패하였습니다.");
		}

		// 생성된 파일 id, 파일 경로, 파일명을 리턴
		return new ResultData("S-1", "파일이 저장되었습니다.", "id", file.getId(), "fileRealPath", targetFilePath, "fileName", targetFileName);
	}

	/* 다중 파일 저장 (Ajax 파일 추가/삭제 요청) */
	public ResultData saveFiles(MultipartRequest multipartRequest, Map<String, Object> paramMap) {

		// 체크된 파일 삭제
		int deleteCount = deleteCheckedFiles(paramMap);

		// A Map containing the Parameter names as Keys, and the MultipartFile objects as Values
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();

		Map<String, Object> saveFileResults = new LinkedHashMap<>();
		List<Integer> fileIds = new ArrayList<>();

		for (String paramName : fileMap.keySet()) {
			MultipartFile multipartFile = fileMap.get(paramName);

			if (! multipartFile.isEmpty()) {
				ResultData saveFileResult = saveFile(multipartFile);
				int fileId = (int) saveFileResult.getBody().get("id");
				fileIds.add(fileId);

				saveFileResults.put(paramName, saveFileResult);
			}
		}

		String fileIdsStr = Joiner.on(",").join(fileIds);
		// String fileIdsStr = StringUtils.join(fileIds, ",")
		// String fileIdsStr = fileIds.stream().map(String::valueOf).collect(Collectors.joining(","));

		return new ResultData("S-1", " 파일을 업로드하였습니다.", "saveFileResults", saveFileResults, "fileIdsStr", fileIdsStr, "deleteCount", deleteCount);
	}

	private int deleteCheckedFiles(Map<String, Object> paramMap) {
		// 삭제한 파일 개수 리턴
		int deleteCount = 0;

		for (String paramName : paramMap.keySet()) {
			String[] paramNameBits = paramName.split("__");
			boolean isValidParam = paramNameBits[0].equals("deleteFile"); // 체크박스의 파라미터명이 deleteFile 로 시작

			if (isValidParam) {
				String relTypeCode = paramNameBits[1];
				int relId = Integer.parseInt(paramNameBits[2]);
				String typeCode = paramNameBits[3];
				String type2Code = paramNameBits[4];
				int fileNo = Integer.parseInt(paramNameBits[5]);

				AttachFile oldFile = getOneByFileNo(relTypeCode, relId, typeCode, type2Code, fileNo);

				if (oldFile != null) {
					deleteFile(oldFile);
					deleteCount++;
				}
			}
		}
		return deleteCount;
	}

	private void checkAndDeleteOldFileIfExists(AttachFile file) {
		AttachFile oldFile = getOneByFileNo(file.getRelTypeCode(), file.getRelId(), file.getTypeCode(), file.getType2Code(), file.getFileNo());

		if (oldFile != null) {
			deleteFile(oldFile);
		}
	}

}
