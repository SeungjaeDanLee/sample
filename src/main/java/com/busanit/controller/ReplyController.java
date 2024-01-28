package com.busanit.controller;

import com.busanit.domain.ReplyDTO;
import com.busanit.entity.Reply;
import com.busanit.repository.ReplyRepository;
import com.busanit.service.ReplyService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/replies/")
@Log4j2
@AllArgsConstructor
public class ReplyController {

    private ReplyService replyService;
    private ReplyRepository replyRepository;

    // 댓글 리스트 조회
    @GetMapping(value = "/board/{bno}", produces = MediaType.APPLICATION_JSON_VALUE)
    //public ResponseEntity<List<ReplyDTO>> getListByBoard(
    public ResponseEntity<Slice<Reply>> getListByBoard(
                                            @PathVariable("bno") Long bno,
                                            @PageableDefault(size = 5, sort = "rno", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("bno: " + bno);

        //return new ResponseEntity<>(replyService.getList(bno), HttpStatus.OK);
        return new ResponseEntity<>(replyRepository.findByBoard_Bno(bno, pageable), HttpStatus.OK);

    }

    // @RequestBody - JSON으로 들어오는 데이터를 자동으로 해당 타입의 객체로
    //                매핑해주는 역할을 담당함
    // 댓글 등록
    @PostMapping("")
    public ResponseEntity<Long> register(@RequestBody ReplyDTO replyDTO){

        log.info(replyDTO);

        Long rno = replyService.register(replyDTO);

        return new ResponseEntity<>(rno, HttpStatus.OK);
    }

    // 댓글 삭제
    @DeleteMapping("/{rno}")
    public ResponseEntity<String> remove(@PathVariable("rno") Long rno) {

        log.info("RNO:" + rno );

        replyService.remove(rno);

        return new ResponseEntity<>("success", HttpStatus.OK);

    }

    // 댓글 수정
    @PutMapping("/{rno}")
    public ResponseEntity<String> modify(@RequestBody ReplyDTO replyDTO) {

        log.info(replyDTO);

        replyService.modify(replyDTO);

        return new ResponseEntity<>("success", HttpStatus.OK);

    }
}





