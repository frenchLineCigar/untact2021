package com.tena.untact2021.service;

import com.tena.untact2021.dao.FileDao;
import com.tena.untact2021.dto.File;
import com.tena.untact2021.dto.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FileService {

	private final FileDao fileDao;

	/* 파일 정보 저장 */
	public void saveFileMetaInfo(File file) {
		fileDao.save(file);
	}
}
