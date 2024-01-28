package com.busanit.domain;

import com.busanit.entity.BoardAttach;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class BoardDTO {

    private Long bno;
    private String title;
    private String content;
    private String writer;
    private LocalDateTime regDate;
    private LocalDateTime updateDate;

    // 첨부파일 테이블(board_attach)의 PK
    private List<Long> attachNo;
    // 첨부파일의 이름들
    private List<String> fileNames;
    // 썸네일 파일의 이름들
    private List<String> thumbnailNames;
}
