package com.tena.untact2021.service;

import com.tena.untact2021.dao.MemberDao;
import com.tena.untact2021.dto.ResultData;
import com.tena.untact2021.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberDao memberDao;

    /* 회원 가입 */
    public ResultData joinMember(Map<String, Object> param) {
        memberDao.save(param);

        int id = Util.getAsInt(param.get("id"));

        return new ResultData("S-1", String.format("%s님 환영합니다.", param.get("nickname")), "id", id);
    }
}
