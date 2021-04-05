package com.tena.untact2021.controller.admin;

import com.tena.untact2021.dto.Member;
import com.tena.untact2021.dto.ResultData;
import com.tena.untact2021.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * TODO : 리팩토링 해야할 것
 *  -> 바인딩 및 유효성 검증 : JSR-303 사용(DTO) 및 Validator 구현(@InitBinder) + HandlerMethodArgumentResolver
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class AdminMemberController {

    private final MemberService memberService;

    @Resource(name = "loginMemberBean")
    private Member loginMemberBean; //로그인 사용자 정보를 담을 세션 스코프 빈
    
    /* 관리자 로그인 */
    @RequestMapping("/admin/member/doLogin")
    @ResponseBody
    public ResultData doLogin(String loginId, String loginPw) {
        if (loginId == null) return new ResultData("F-1", "loginId를 입력해주세요.");
        if (loginPw == null) return new ResultData("F-1", "비밀번호를 입력해주세요.");

        // 해당 회원 조회
        Member existingMember = memberService.getMemberByLoginId(loginId);
        if (existingMember == null) return new ResultData("F-2", "존재하지 않는 로그인 아이디 입니다.", "loginId", loginId);

        // 비밀번호 일치 여부 체크
        if (!existingMember.getLoginPw().equals(loginPw)) return new ResultData("F-3", "비밀번호가 일치하지 않습니다.");

        // 해당 회원이 관리자인지 체크
        if (!existingMember.isAdmin()) return new ResultData("F-4", "관리자만 접근할 수 있습니다.");

        // 로그인 처리
        memberService.login(existingMember);

        return new ResultData("S-1", String.format("%s님 환영합니다.", existingMember.getNickname()));
    }

    /* 관리자 로그아웃 */
    @RequestMapping("/admin/member/doLogout")
    @ResponseBody
    public ResultData doLogout(HttpSession session) {
        // 세션 무효화
        session.invalidate();

        return new ResultData("S-1", "로그아웃 되었습니다.");
    }

    /* 관리자 정보 수정 */
    @RequestMapping("/admin/member/doModify")
    @ResponseBody
    public ResultData doModify(Member member) {
        if (member.isValidInput()) {
            return new ResultData("F-2", "수정할 정보를 입력해주세요.");
        }

        //수정자는 현재 세션에 로그인한 사용자
        member.setId(loginMemberBean.getId());

        return memberService.modifyMember(member);
    }

}
