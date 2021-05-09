package com.tena.untact2021.dao;

import com.tena.untact2021.dto.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface MemberDao {

    /* 회원 가입 */
    void save(Member member);

    /* 회원 조회 (PK) */
    Member findById(@Param("id") int id);

    /* 회원 조회 (로그인 ID) */
    Member findByLoginId(@Param("loginId") String loginId);

    /* 회원 정보 수정 */
    void update(Member member);

    /* 회원 조회 (authKey) */
    Member findByAuthKey(@Param("authKey") String authKey);

	/* 전체 회원 조회 */
    List<Member> findAllForPrint(@Param("searchKeywordType") String searchKeywordType,
                                 @Param("searchKeyword") String searchKeyword,
                                 @Param("limitFrom") int limitFrom,
                                 @Param("limitTake") int limitTake,
                                 Map<String, Object> param);

}
