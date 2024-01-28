package com.busanit.service;

import com.busanit.domain.ReplyDTO;
import com.busanit.entity.Board;
import com.busanit.entity.Reply;
import com.busanit.repository.ReplyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReplyService {

    private ReplyRepository replyRepository;

    // 댓글 등록
    public Long register(ReplyDTO replyDTO) {
        Reply reply = dtoToEntity(replyDTO);

        replyRepository.save(reply);

        return reply.getRno();
    }

    // 특정 게시글의 댓글 목록
    public List<ReplyDTO> getList(Long bno) {

//        List<Reply> result = replyRepository
//                .getRepliesByBoardOrderByRno(Board.builder().bno(bno).build());
//        return result.stream().map(reply -> entityToDTO(reply))
//                                            .collect(Collectors.toList());
        List<Reply> result = replyRepository
                .findByBoardOrderByRno(Board.builder().bno(bno).build());
        return result.stream().map(reply -> entityToDTO(reply))
                                            .collect(Collectors.toList());
    }

    // 댓글 수정
    public void modify(ReplyDTO replyDTO) {
        Reply reply = dtoToEntity(replyDTO);

        replyRepository.save(reply);
    }

    // 댓글 삭제
    public void remove(Long rno) {
        replyRepository.deleteById(rno);
    }

    // ReplyDTO를 Reply 엔티티로 변환
    private Reply dtoToEntity(ReplyDTO replyDTO) {
        Board board = Board.builder().bno(replyDTO.getBno()).build();

        Reply reply = Reply.builder()
                .rno(replyDTO.getRno())
                .text(replyDTO.getText())
                .replyer(replyDTO.getReplyer())
                .board(board)
                .build();

        return reply;
    }

    // Reply 엔티티를 ReplyDTO로 변환
    ReplyDTO entityToDTO(Reply reply) {
        ReplyDTO dto = ReplyDTO.builder()
                .rno(reply.getRno())
                .text(reply.getText())
                .replyer(reply.getReplyer())
                .regDate(reply.getRegTime())
                .modDate(reply.getUpdateTime())
                .build();

        return dto;
    }
}




