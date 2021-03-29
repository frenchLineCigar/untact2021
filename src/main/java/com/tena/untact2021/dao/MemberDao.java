package com.tena.untact2021.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface MemberDao {

    /* 회원 가입 */
    void save(Map<String, Object> param);

}
