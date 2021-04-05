package com.tena.untact2021.dao;

import com.tena.untact2021.dto.Reply;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReplyDao {
    /* 댓글 추가 */
    void save(Reply reply);

    /* 댓글 조회 */
    List<Reply> findAllForPrint(@Param("relTypeCode") String relTypeCode, @Param("relId") int relId);
}
