package com.tena.untact2021.controller.user;

import com.tena.untact2021.custom.CurrentMember;
import com.tena.untact2021.dto.Member;
import com.tena.untact2021.dto.ResultData;
import com.tena.untact2021.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * TODO : 리팩토링 해야할 것
 *  -> 바인딩 및 유효성 검증 : JSR-303 사용(DTO) 및 Validator 구현(@InitBinder) + HandlerMethodArgumentResolver
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class UserMemberController {

    private final MemberService memberService;

    /* 회원 가입 */
    @RequestMapping("/user/member/doJoin")
    @ResponseBody
    public ResultData doJoin(Member member) {
        if (member.getLoginId() == null) return new ResultData("F-1", "loginId를 입력해주세요.");

        // 아이디 중복 체크
        Member existingMember = memberService.getMemberByLoginId(member.getLoginId());

        if (existingMember != null) {
            return new ResultData("F-2", String.format("%s (은)는 이미 사용 중인 로그인 아이디 입니다.", member.getLoginId()));
        }

        if (member.getLoginPw() == null) return new ResultData("F-1", "loginPw을 입력해주세요.");
        if (member.getName() == null) return new ResultData("F-1", "name을 입력해주세요.");
        if (member.getNickname() == null) return new ResultData("F-1", "nickname을 입력해주세요.");
        if (member.getCellphoneNo() == null) return new ResultData("F-1", "cellphoneNo을 입력해주세요.");
        if (member.getEmail() == null) return new ResultData("F-1", "email을 입력해주세요.");

        return memberService.joinMember(member);
    }

    /* 인증키(authKey) 조회 */
    @RequestMapping("/user/member/authKey")
    @ResponseBody
    public ResultData showAuthKey(String loginId, String loginPw) {
        if (loginId == null) return new ResultData("F-1", "loginId를 입력해주세요.");
        if (loginPw == null) return new ResultData("F-1", "loginPw를 입력해주세요.");

        // 해당 회원 조회
        Member existingMember = memberService.getMemberByLoginId(loginId);
        if (existingMember == null) return new ResultData("F-2", "존재하지 않는 로그인 아이디 입니다.", "loginId", loginId);

        // 비밀번호 일치 여부 체크
        if (!existingMember.getLoginPw().equals(loginPw)) return new ResultData("F-3", "아이디 또는 비밀번호가 일치하지 않습니다.");

        return new ResultData("S-1", String.format("%s님 환영합니다.", existingMember.getNickname()), "authKey", existingMember.getAuthKey());
    }


    /* 회원 로그인 */
    @RequestMapping("/user/member/doLogin")
    @ResponseBody
    public ResultData doLogin(String loginId, String loginPw) {
        if (loginId == null) return new ResultData("F-1", "loginId를 입력해주세요.");
        if (loginPw == null) return new ResultData("F-1", "loginPw를 입력해주세요.");

        // 해당 회원 조회
        Member existingMember = memberService.getMemberByLoginId(loginId);
        if (existingMember == null) return new ResultData("F-2", "존재하지 않는 로그인 아이디 입니다.", "loginId", loginId);

        // 비밀번호 일치 여부 체크
        if (!existingMember.getLoginPw().equals(loginPw)) return new ResultData("F-3", "아이디 또는 비밀번호가 일치하지 않습니다.");

        // 로그인 처리
        memberService.login(existingMember);

        return new ResultData("S-1", String.format("%s님 환영합니다.", existingMember.getNickname()));
    }

    /* 회원 로그아웃 */
    @RequestMapping("/user/member/doLogout")
    @ResponseBody
    public ResultData doLogout(HttpSession session) {
        // 세션 무효화
        session.invalidate();

        return new ResultData("S-1", "로그아웃 되었습니다.");
    }

    /* 회원 정보 수정 */
    @RequestMapping("/user/member/doModify")
    @ResponseBody
    public ResultData doModify(Member member, @CurrentMember Member currentMember) {
        if (member.isValidInput()) {
            return new ResultData("F-2", "수정할 정보를 입력해주세요.");
        }

        //수정자는 현재 인증된 사용자
        member.setId(currentMember.getId());

        return memberService.modifyMember(member);
    }

}
