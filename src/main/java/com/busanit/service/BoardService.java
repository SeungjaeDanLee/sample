package com.busanit.service;

import com.busanit.entity.Board;
import com.busanit.repository.BoardRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class BoardService {
    private BoardRepository boardRepository;

    public Page<Board> getBoardList(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    public Page<Board> getBoardTitleList(String keyword, Pageable pageable) {
        return boardRepository.findByTitleContaining(keyword, pageable);
    }

    public Page<Board> getBoardContentList(String keyword, Pageable pageable) {
        return boardRepository.findByContentContaining(keyword, pageable);
    }
}





