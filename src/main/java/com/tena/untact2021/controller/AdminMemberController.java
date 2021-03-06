package com.tena.untact2021.controller;

import com.tena.untact2021.custom.CurrentMember;
import com.tena.untact2021.dto.Member;
import com.tena.untact2021.dto.ResultData;
import com.tena.untact2021.service.MemberService;
import com.tena.untact2021.util.Util;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * TODO : 리팩토링 해야할 것
 *  -> 바인딩 및 유효성 검증 : JSR-303 사용(DTO) 및 Validator 구현(@InitBinder) + HandlerMethodArgumentResolver
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class AdminMemberController extends BaseController {

    private final MemberService memberService;

    /* 가입 시 로그인 아이디 중복 체크, 유효성 체크 */
    @GetMapping("/admin/member/checkLoginIdDuplicate")
    @ResponseBody
    public ResultData checkLoginIdDuplicate(String loginId) {
        if (loginId == null) {
            return new ResultData("F-1", "로그인 아이디를 입력해주세요.");
        }

        if (Util.allNumberString(loginId)) {
            return new ResultData("F-2", "로그인 아이디는 숫자만으로 구성될 수 없습니다.");
        }

        if (Util.startsWithNumber(loginId)) {
            return new ResultData("F-3", "로그인 아이디는 숫자로 시작할 수 없습니다.");
        }

        if (loginId.length() < 5) {
            return new ResultData("F-4", "로그인 아이디는 최소 5자 이상으로 입력해주세요.");
        }

        if (loginId.length() > 20) {
            return new ResultData("F-5", "로그인 아이디는 최대 20자 이하로 입력해주세요.");
        }

        if (! Util.isStandardLoginIdString(loginId)) {
            return new ResultData("F-6", "로그인 아이디는 영문자와 숫자만으로 구성되어야 합니다.");
        }

        Member existingMember = memberService.getMemberByLoginId(loginId);
        if (existingMember != null) {
            return new ResultData("F-2", String.format("%s(은)는 이미 사용 중인 로그인 아이디입니다.", loginId));
        }

        return new ResultData("S-1", String.format("%s(은)는 사용가능한 로그인 아이디 입니다.", loginId), "loginId", loginId);
    }

    /* 회원 관리 리스트 */
    @GetMapping("/admin/member/list")
    public String showList(@RequestParam(defaultValue = "1") int boardId, @RequestParam(defaultValue = "1") int page,
                           @RequestParam Map<String, Object> param, String searchKeywordType, String searchKeyword, Model model) {

        if (searchKeywordType != null) {
            searchKeywordType = searchKeywordType.trim();
        }

        if (searchKeywordType == null || searchKeywordType.length() == 0) {
            searchKeywordType = "name";
        }

        if (searchKeyword != null && searchKeyword.length() == 0) {
            searchKeyword = null;
        }

        if (searchKeyword != null) {
            searchKeyword = searchKeyword.trim();
        }

        if (searchKeyword == null) {
            searchKeywordType = null;
        }

        int itemsInAPage = 20;

        List<Member> members = memberService.getForPrintMembers(searchKeywordType, searchKeyword, page, itemsInAPage, param);

        model.addAttribute("members", members);

        return "admin/member/list";
    }

    @GetMapping("/admin/member/join")
    public String showJoin() {

        return "admin/member/join";
    }

    @PostMapping("/admin/member/doJoin")
    @ResponseBody
    public String doJoin(Member member) {

        if (member.getLoginId() == null) return msgAndBack("loginId를 입력해주세요.");

        // 아이디 중복 체크
        Member existingMember = memberService.getMemberByLoginId(member.getLoginId());
        if (existingMember != null) return msgAndBack("이미 사용 중인 로그인 아이디입니다.");

        if (member.getLoginPw() == null) return msgAndBack("loginPw를 입력해주세요.");
        if (member.getName() == null) return msgAndBack("name을 입력해주세요.");
        if (member.getNickname() == null) return msgAndBack("nickname을 입력해주세요.");
        if (member.getEmail() == null) return msgAndBack("email을 입력해주세요.");
        if (member.getCellphoneNo() == null) return msgAndBack("cellphoneNo를 입력해주세요.");


        // 가입 처리
        memberService.joinMember(member);

        String msg = String.format("%s님 환영합니다.", member.getNickname());
        String redirectUrl = "./login";

        return msgAndReplace(msg, redirectUrl);
    }

    @RequestMapping("/admin/member/login")
    public String showLogin() {

        return "admin/member/login";
    }

    /* 관리자 로그인 */
    @PostMapping("/admin/member/doLogin")
    @ResponseBody
    @ApiOperation(value = "관리자 로그인", notes = "성공시 로그인 상태가 됩니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginId", value ="로그인 아이디", required = true),
            @ApiImplicitParam(name = "loginPw", value ="로그인 비밀번호", required = true)
    })
    public String doLogin(String loginId, String loginPw, String redirectUrl) {
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

        return msgAndReplace(msg, redirectUrl);
    }

    /* 관리자 로그아웃 */
    @GetMapping("/admin/member/doLogout")
    @ResponseBody
    public String doLogout(HttpSession session) {
        // 세션 무효화
        session.invalidate();

        return msgAndReplace("로그아웃 되었습니다.", "../member/login");
    }

    /* 회원 정보 수정 폼 */
    @GetMapping("/admin/member/modify")
    public String showModify(Integer id, Model model) {
        if (id == null) return msgAndBack(model, "id를 입력해주세요.");

        // 해당 회원 조회
        Member member = memberService.getForPrintMember(id);

        if (member == null) return msgAndBack(model, "존재하지 않는 회원번호 입니다.");

        model.addAttribute("member", member);

        return "admin/member/modify";
    }

    /* 관리자 정보 수정 */
    @PostMapping("/admin/member/doModify")
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
