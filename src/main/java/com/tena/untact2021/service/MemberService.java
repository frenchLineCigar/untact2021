package com.tena.untact2021.service;

import com.tena.untact2021.dao.MemberDao;
import com.tena.untact2021.dto.AttachFile;
import com.tena.untact2021.dto.Member;
import com.tena.untact2021.dto.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberDao memberDao;
    private final AttachFileService fileService;

    @Resource(name = "loginMemberBean")
    private Member loginMemberBean; //로그인 사용자 정보를 담을 세션 스코프 빈

    /* 회원 가입 */
    public ResultData joinMember(Member newMember) {
        memberDao.save(newMember);

        changeInputFilesRelId(newMember);

        return new ResultData("S-1", String.format("%s님 환영합니다.", newMember.getNickname()), "id", newMember.getId());
    }

    private void changeInputFilesRelId(Member member) {
        if (member.hasInputFiles()) {
            String fileIdsStr = member.getFileIdsStr(); // Ajax로 업로드한 파일 번호가 담긴 문자열
            Integer articleId = member.getId();
            fileService.changeRelIds(fileIdsStr, articleId);
        }
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

    /* 회원 조회 (인증키) */
    public Member getMemberByAuthKey(String authKey) {
        return memberDao.findByAuthKey(authKey);
    }

    /* 회원 관리 리스트 */
    public List<Member> getForPrintMembers(String searchKeywordType, String searchKeyword, int page, int itemsInAPage, Map<String, Object> param) {
        int limitFrom = (page - 1) * itemsInAPage;
        int limitTake = itemsInAPage;

        return memberDao.findAllForPrint(searchKeywordType, searchKeyword, limitFrom, limitTake, param);
    }

    public Member getForPrintMember(Integer id) {
        return memberDao.findForPrintById(id);
    }

    public Member getForPrintMemberByAuthKey(String authKey) {
        Member member = memberDao.findByAuthKey(authKey);

        addThumbUrlForPrint(member);

        return member;
    }

    public Member getForPrintMemberByLoginId(String loginId) {
        Member member = memberDao.findByLoginId(loginId);

        addThumbUrlForPrint(member);

        return member;
    }

    private void addThumbUrlForPrint(Member member) {
        AttachFile file = fileService.getFile("member", member.getId(), "common", "attachment", 1);

        if (file != null) {
            String extra__thumbUrl = file.getForPrintUrl();
            member.setExtra__thumbUrl(extra__thumbUrl);
        }
    }

//    public ResultData joinAdmin(Member newAdmin) {
//        newAdmin.setAuthLevel(AuthLevel.ADMIN.getValue());
//        memberDao.save(newAdmin);
//        return new ResultData("S-1", String.format("%s님 환영합니다.", newAdmin.getNickname()));
//    }

}
