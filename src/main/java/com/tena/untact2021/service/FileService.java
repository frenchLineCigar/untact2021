package com.tena.untact2021.service;

import com.tena.untact2021.dao.FileDao;
import com.tena.untact2021.dto.AttachFile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FileService {

	private final FileDao fileDao;

	/* 단일 파일 저장 */
	public void saveFile(AttachFile attachFile) {
		fileDao.save(attachFile);
	}

	/* 다수 파일 저장 */
	public void saveFiles(List<AttachFile> attachFiles) {
		for (AttachFile attachFile : attachFiles) {
			fileDao.save(attachFile);
		}
	}
}
