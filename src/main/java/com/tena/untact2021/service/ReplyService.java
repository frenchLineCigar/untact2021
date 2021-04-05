package com.tena.untact2021.service;

import com.tena.untact2021.dao.ReplyDao;
import com.tena.untact2021.dto.Member;
import com.tena.untact2021.dto.Reply;
import com.tena.untact2021.dto.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyDao replyDao;

    @Resource(name = "loginMemberBean")
    private Member loginMemberBean;

    /* 댓글 추가 */
    public ResultData addReply(Reply reply) {
        replyDao.save(reply);

        return new ResultData("S-1", "성공하였습니다.", "id", reply.getId());
    }

    /* 댓글 조회 */
    public List<Reply> getForPrintReplies(String relTypeCode, int relId) {
        return replyDao.findAllForPrint(relTypeCode, relId);
    }

    /* 특정 댓글 1개 */
    private Reply getReply(int id) {
        return replyDao.findById(id);
    }

    /* 댓글 삭제 */
    public ResultData deleteReply(int id) {
        //댓글 유무 확인
        Reply existingReply = getReply(id);
        if (existingReply == null) {
            return new ResultData("F-1", "해당 댓글은 존재하지 않습니다.");
        }

        //삭제 권한 체크
        if (! loginMemberBean.canDelete(existingReply)) {
            return new ResultData("F-1", "권한이 없습니다.");
        }

        //댓글 삭제 처리
        boolean result = replyDao.deleteById(existingReply.getId());

        if (result == false) {
            return new ResultData("F-2", "삭제에 실패했습니다.", "id", existingReply.getId());
        }

        return new ResultData("S-1", "삭제하였습니다.", "id", existingReply.getId());
    }

}
