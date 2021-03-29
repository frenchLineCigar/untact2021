package com.tena.untact2021.dao;

import com.tena.untact2021.dto.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface MemberDao {

    /* 회원 가입 */
    void save(Map<String, Object> param);

    /* 회원 조회 (PK) */
    Member findById(@Param("id") int id);

    /* 회원 조회 (로그인 ID) */
    Member findByLoginId(@Param("loginId") String loginId);

}
