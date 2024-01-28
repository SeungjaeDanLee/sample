package com.busanit.repository;

import com.busanit.entity.BoardAttach;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardAttachRepository extends JpaRepository<BoardAttach, Long> {

    List<BoardAttach> findByBoard_Bno(Long bno);
}
