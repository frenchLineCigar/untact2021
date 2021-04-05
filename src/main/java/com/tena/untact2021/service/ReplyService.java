package com.tena.untact2021.service;

import com.tena.untact2021.dao.ReplyDao;
import com.tena.untact2021.dto.Reply;
import com.tena.untact2021.dto.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyDao replyDao;

    /* 댓글 추가 */
    public ResultData addReply(Reply reply) {
        replyDao.save(reply);

        return new ResultData("S-1", "성공하였습니다.", "id", reply.getId());
    }

    /* 댓글 조회 */
    public List<Reply> getForPrintReplies(String relTypeCode, int relId) {
        return replyDao.findAllForPrint(relTypeCode, relId);
    }

}
