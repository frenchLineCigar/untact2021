package com.tena.untact2021.dao;

import com.tena.untact2021.dto.AttachFile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FileDao {

	/* 파일 정보 저장 */
	void save(AttachFile attachFile);

	/* 썸네일 가져오기 */
	AttachFile findFileByThumbnailCondition(@Param("relTypeCode") String relTypeCode,
	                                        @Param("relId") int relId,
	                                        @Param("typeCode") String typeCode,
	                                        @Param("type2Code") String type2Code,
	                                        @Param("fileNo") int fileNo);

}
