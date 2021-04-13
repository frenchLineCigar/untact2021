package com.tena.untact2021.controller.admin;

import com.tena.untact2021.controller.VCG;
import com.tena.untact2021.custom.CurrentMember;
import com.tena.untact2021.dto.Member;
import com.tena.untact2021.dto.ResultData;
import com.tena.untact2021.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * TODO : 리팩토링 해야할 것
 *  -> 바인딩 및 유효성 검증 : JSR-303 사용(DTO) 및 Validator 구현(@InitBinder) + HandlerMethodArgumentResolver
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class AdminMemberController extends VCG {

    private final MemberService memberService;

    @RequestMapping("/admin/member/login")
    public String login() {

        return "admin/member/login";
    }

    /* 관리자 로그인 */
    @RequestMapping("/admin/member/doLogin")
    @ResponseBody
    public String doLogin(String loginId, String loginPw) {
        if (loginId == null) return msgAndBack("loginId를 입력해주세요.");
        if (loginPw == null) return msgAndBack("비밀번호를 입력해주세요.");

        // 해당 회원 조회
        Member existingMember = memberService.getMemberByLoginId(loginId);
        if (existingMember == null) return msgAndBack("존재하지 않는 로그인 아이디 입니다.");

        // 비밀번호 일치 여부 체크
        if (!existingMember.getLoginPw().equals(loginPw)) return msgAndBack("비밀번호가 일치하지 않습니다.");

        // 해당 회원이 관리자인지 체크
        if (!existingMember.isAdmin()) return msgAndBack("관리자만 접근할 수 있습니다.");

        // 로그인 처리
        memberService.login(existingMember);

        String msg = String.format("%s님 환영합니다.", existingMember.getNickname());

        return msgAndReplace(msg, "../home/main");
    }

    /* 관리자 정보 수정 */
    @RequestMapping("/admin/member/doModify")
    @ResponseBody
    public ResultData doModify(Member member, @CurrentMember Member currentMember) {
        if (! member.isValidInput()) {
            return new ResultData("F-2", "수정할 정보를 입력해주세요.");
        }

        //수정자는 현재 인증된 사용자
        member.setId(currentMember.getId());

        return memberService.modifyMember(member);
    }

}
