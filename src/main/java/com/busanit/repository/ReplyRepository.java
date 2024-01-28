package com.busanit.repository;

import com.busanit.entity.Board;
import com.busanit.entity.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    // Board 삭제 시에 댓글들도 삭제
    @Modifying
    @Query("DELETE FROM Reply r WHERE r.board.bno = :bno ")
    void deleteByBno(Long bno);

    // 게시물로 댓글 목록 가져오기
//    List<Reply> getRepliesByBoardOrderByRno(Board board);
    List<Reply> findByBoardOrderByRno(Board board);

    Slice<Reply> findByBoard_Bno(Long bno, Pageable pageable);

}


