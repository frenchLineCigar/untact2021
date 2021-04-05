package com.tena.untact2021.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tena.untact2021.dto.Member;
import com.tena.untact2021.dto.ResultData;
import com.tena.untact2021.service.ArticleService;
import com.tena.untact2021.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component("checkWriterInterceptor")
@RequiredArgsConstructor
public class CheckWriterInterceptor implements HandlerInterceptor {

    private final ArticleService articleService;
    private final ReplyService replyService;

    @Resource(name = "loginMemberBean")
    private Member loginMemberBean;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 컨텐츠 수정, 삭제 URL 직접 호출에 대한 권한 검증 및 처리
     *  1. 현재 로그인 사용자가 작성자와 같을 경우
     *  2. 관리자인 경우
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("CheckWriterInterceptor.preHandle");

        //게시물 or 댓글 번호
        String idStr = request.getParameter("id");
        int id = Integer.parseInt(idStr);
        System.out.println("id = " + id);

        //작성자 번호 조회
        int writerId = 0;
        String requestURI = request.getRequestURI();
        System.out.println("requestURI = " + requestURI);
        if (requestURI.contains("/article")) {
            //게시물인 경우
            System.out.println("/article");
            writerId = articleService.getArticle(id).getMemberId();
        }

        if (requestURI.contains("/reply")) {
            //댓글인 경우
            writerId = replyService.getReply(id).getMemberId();
            System.out.println("/reply");
        }
        System.out.println("writerId = " + writerId);


        //수정, 삭제 가능 여부 체크
        if (! (loginMemberBean.isSameMember(writerId) || loginMemberBean.isAdmin()) ){
            response.setContentType("application/json; charset=UTF-8");
            ResultData resultData = new ResultData("F-1", "권한이 없습니다.!!!!!!!");
            String resultJson = objectMapper.writeValueAsString(resultData);
            response.getWriter().append(resultJson);

            return false;
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}