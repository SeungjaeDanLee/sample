package com.busanit.service;

import com.busanit.entity.BoardAttach;
import com.busanit.repository.BoardAttachRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class BoardAttachService {
    private BoardAttachRepository boardAttachRepository;

    public void saveBoardAttachList(List<BoardAttach> list) {
        for(BoardAttach attach : list) {
            boardAttachRepository.save(attach);
        }
    }
}
