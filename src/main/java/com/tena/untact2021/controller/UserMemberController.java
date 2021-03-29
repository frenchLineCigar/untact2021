package com.tena.untact2021.controller;

import com.tena.untact2021.dto.Member;
import com.tena.untact2021.dto.ResultData;
import com.tena.untact2021.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.acl.Group;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class UserMemberController {

    private final MemberService memberService;

    /* 회원 가입 */
    @RequestMapping("/user/member/doJoin")
    @ResponseBody
    public ResultData doJoin(@RequestParam Map<String, Object> param) {

        // TODO : 리팩토링
        //  HandlerMethodArgumentResolver + JSR-303 사용(DTO) 및 Validator 구현(@InitBinder)

        if (param.get("loginId") == null) {
            return new ResultData("F-1", "loginId를 입력해주세요.");
        }

        Member existingMember = memberService.getMemberByLoginId((String) param.get("loginId"));

        if (existingMember != null) {
            return new ResultData("F-2", String.format("%s (은)는 이미 사용 중인 로그인 아이디 입니다.", param.get("loginId")));
        }

        if (param.get("loginPw") == null) {
            return new ResultData("F-1", "loginPw을 입력해주세요.");
        }

        if (param.get("name") == null) {
            return new ResultData("F-1", "name을 입력해주세요.");
        }

        if (param.get("nickname") == null) {
            return new ResultData("F-1", "nickname을 입력해주세요.");
        }

        if (param.get("cellphoneNo") == null) {
            return new ResultData("F-1", "cellphoneNo을 입력해주세요.");
        }

        if (param.get("email") == null) {
            return new ResultData("F-1", "email을 입력해주세요.");
        }

        return memberService.joinMember(param);
    }

    /* 회원 로그인 */
    @RequestMapping("/user/member/doLogin")
    @ResponseBody
    public ResultData doLogin(String loginId, String loginPw, HttpSession session) {

        if (session.getAttribute("loggedInMemberId") != null) {
            return new ResultData("F-4", "이미 로그인 되었습니다.");
        }

        // TODO : 리팩토링
        //  HandlerMethodArgumentResolver + JSR-303 사용(DTO) 및 Validator 구현(@InitBinder)

        if (loginId == null) {
            return new ResultData("F-1", "loginId를 입력해주세요.");
        }

        Member existingMember = memberService.getMemberByLoginId((String) loginId);

        if (existingMember == null) {
            return new ResultData("F-2", "존재하지 않는 로그인 아이디 입니다.", "loginId", loginId);
        }

        if (loginPw == null) {
            return new ResultData("F-1", "비밀번호를 입력해주세요.");
        }

        if (existingMember.getLoginPw().equals(loginPw) == false) {
            return new ResultData("F-3", "비밀번호가 일치하지 않습니다.");
        }

        session.setAttribute("loggedInMemberId", existingMember.getId());

        return new ResultData("S-1", String.format("%s님 환영합니다.", existingMember.getNickname()));
    }

    /* 회원 로그아웃 */
    @RequestMapping("/user/member/doLogout")
    @ResponseBody
    public ResultData doLogout(HttpSession session) {

        if (session.getAttribute("loggedInMemberId") == null) {
            return new ResultData("S-2", "이미 로그아웃 되었습니다.");
        }

        //session.removeAttribute("loggedInMemberId");
        session.invalidate();

        return new ResultData("S-1", "로그아웃 되었습니다.");
    }

}
