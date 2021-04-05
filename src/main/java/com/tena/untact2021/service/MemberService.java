package com.tena.untact2021.service;

import com.tena.untact2021.dao.MemberDao;
import com.tena.untact2021.dto.Member;
import com.tena.untact2021.dto.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberDao memberDao;

    @Resource(name = "loginMemberBean")
    private Member loginMemberBean; //로그인 사용자 정보를 담을 세션 스코프 빈

    /* 회원 가입 */
    public ResultData joinMember(Member newMember) {
        memberDao.save(newMember);
        return new ResultData("S-1", String.format("%s님 환영합니다.", newMember.getNickname()), "id", newMember.getId());
    }

    /* 회원 조회 (PK) */
    public Member getMemberById(int id) {
        return memberDao.findById(id);
    }

    /* 회원 조회 (로그인 ID) */
    public Member getMemberByLoginId(String loginId) {
        return memberDao.findByLoginId(loginId);
    }

    /* 회원 정보 수정 */
    public ResultData modifyMember(Member member) {
        memberDao.update(member);
        return new ResultData("S-1", "회원정보가 수정되었습니다.");
    }

    /* 회원 로그인 */
    public void login(Member existingMember) {
        //세션 영역에서 사용할 빈(loginMemberBean)에 사용자 정보 저장
        loginMemberBean.setId(existingMember.getId());
        loginMemberBean.setLoginId(existingMember.getLoginId());
        loginMemberBean.setLoginStatus(true);
    }
}
