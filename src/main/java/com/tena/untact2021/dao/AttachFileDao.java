package com.tena.untact2021.dao;

import com.tena.untact2021.dto.AttachFile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AttachFileDao {

	/* 파일 정보 저장 */
	void save(AttachFile attachFile);

	AttachFile findById(int id);

	/* 썸네일 가져오기 */
	AttachFile findThumbnail(String relTypeCode, int relId, String typeCode, String type2Code);

	/* 특정 파일 1개 (FileNo 로 특정) */
	AttachFile findFile(@Param("relTypeCode") String relTypeCode, @Param("relId") int relId, @Param("typeCode") String typeCode, @Param("type2Code") String type2Code,
						@Param("fileNo") int fileNo);

	/* 파일이 첨부된 게시물 번호(relId) 수정 */
	void updateRelId(@Param("id") int id, @Param("relId") int relId);

	/* 파일 조회 */
	List<AttachFile> findFiles(@Param("relTypeCode") String relTypeCode,
	                           @Param("relId") int relId,
	                           @Param("typeCode") String typeCode, @Param("type2Code") String type2Code);

	List<AttachFile> findFilesByRelIds(@Param("relTypeCode") String relTypeCode,
	                                   @Param("relIds") List<Integer> relIds,
	                                   @Param("typeCode") String typeCode, @Param("type2Code") String type2Code);

	void deleteFiles(@Param("relTypeCode") String relTypeCode, @Param("relId") int relId);

	void deleteFile(@Param("id") int id);
}
