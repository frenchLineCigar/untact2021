package com.tena.untact2021.controller.user;

import com.tena.untact2021.dto.Article;
import com.tena.untact2021.dto.Member;
import com.tena.untact2021.dto.Reply;
import com.tena.untact2021.dto.ResultData;
import com.tena.untact2021.service.ArticleService;
import com.tena.untact2021.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * TODO : 리팩토링 해야할 것
 *  -> 바인딩 및 유효성 검증 : JSR-303 사용(DTO) 및 Validator 구현(@InitBinder) + HandlerMethodArgumentResolver
 *  -> relTypeCode : 카테고리 좀 만들어지면 Enum 타입으로 만들 것
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class UserReplyController {

	private final ReplyService replyService;
    private final ArticleService articleService;

    @Resource(name = "loginMemberBean")
    private Member loginMemberBean;

    @ExceptionHandler
    @ResponseBody
    public ResultData userReplyExceptionHandler(Exception e) {
        return new ResultData("Exception occurred.", "Bad Request");
    }

    /* 댓글 추가 */
    @RequestMapping("/user/reply/doAdd")
    @ResponseBody
    public ResultData doAdd(Reply reply) {
        if (reply.getBody() == null) return new ResultData("F-1", "내용을 입력해주세요.");
        if (reply.getRelTypeCode() == null) return new ResultData("F-1", "게시판을 지정해주세요.");
        if (reply.getRelId() == null) return new ResultData("F-1", "게시물 번호가 누락되었습니다.");

        //작성자 정보는 현재 세션에 로그인한 사용자
        reply.setMemberId(loginMemberBean.getId());

        return replyService.addReply(reply);
    }

	/* 댓글 조회 */
	@RequestMapping("/user/reply/list")
	@ResponseBody
	public ResultData showList(String relTypeCode, Integer relId) {
        if (relTypeCode == null) return new ResultData("F-1", "게시판을 지정해주세요.");
        if (relId == null) return new ResultData("F-1", "게시물 번호를 입력해주세요.");

        if (relTypeCode.equals("article")) {
            Article article = articleService.getArticle(relId);
            if (article == null) return new ResultData("F-1", "존재하지 않는 게시물 입니다.");
        }

        List<Reply> replies = replyService.getForPrintReplies(relTypeCode, relId);
        return new ResultData("S-1", "조회 결과", "replies", replies);
	}

    /* 댓글 삭제 */
    @RequestMapping("/user/reply/doDelete")
    @ResponseBody
    public ResultData doDelete(Integer id) {
        if (id == null) return new ResultData("F-1", "id를 입력해주세요.");

        //댓글 유무 체크
        Reply existingReply = replyService.getReply(id);
        if (existingReply == null) {
            return new ResultData("F-1", "해당 댓글은 존재하지 않습니다.");
        }

        return replyService.deleteReply(id);
    }

    /* 댓글 수정 */
    @RequestMapping("/user/reply/doModify")
    @ResponseBody
    public ResultData doModify(Reply reply) {
        if (reply.getId() == null) return new ResultData("F-1", "id를 입력해주세요.");
        if (reply.getBody() == null) return new ResultData("F-1", "body을 입력해주세요.");

        //댓글 유무 체크
        Reply existingReply = replyService.getReply(reply.getId());
        if (existingReply == null) {
            return new ResultData("F-1", "해당 댓글은 존재하지 않습니다.");
        }

        return replyService.modifyReply(reply);
    }

}