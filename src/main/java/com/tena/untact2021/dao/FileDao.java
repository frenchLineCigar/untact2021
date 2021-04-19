package com.tena.untact2021.dao;

import com.tena.untact2021.dto.File;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileDao {

	/* 파일 정보 저장 */
	void save(File file);

}
