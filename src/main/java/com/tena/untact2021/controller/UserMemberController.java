package com.tena.untact2021.controller;

import com.tena.untact2021.dto.Member;
import com.tena.untact2021.dto.ResultData;
import com.tena.untact2021.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
        if (param.get("loginId") == null) {
            return new ResultData("F-1", "loginId를 입력해주세요.");
        }

        Member exsistingMember = memberService.getMemberByLoginId((String) param.get("loginId"));

        if (exsistingMember != null) {
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

}
