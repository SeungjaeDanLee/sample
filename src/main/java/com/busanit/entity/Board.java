package com.busanit.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

/*
Entity - 클래스를 엔티티로 선언
Table - 엔티티와 매핑할 테이블을 지정
Id - 테이블의 기본키에 사용할 속성을 지정
GeneratedValue - 키 값을 생성하는 전략 명시
Column - 필드와 컬럼 매핑
Lob - BLOB, CLOB 타입 매핑
CreationTimestamp - insert 시 시간 자동 저장
UpdateTimestamp - update 시 시간 자동 저장
Enumerated - enum 타입 매칭
Transient - 해당 필드 데이터베이스 매핑 무시
Temporal - 날짜 타입 매칭
CreateDate - 엔티티가 생성되어 저장될 때 시간 자동 저장
LastModifiedDate - 조회한 엔티티의 값을 변경할 때 시간 자동 저장
 */
@Entity
@Table(name="board")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Board extends BaseEntity{
    @Id
    @Column(name="bno")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long bno;

    @Column(nullable = false)
    private String title;

    private String content;

    @Column(nullable = false)
    private String writer;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime regDate;

    @UpdateTimestamp
    private LocalDateTime updateDate;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private List<Reply> replyList;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private List<BoardAttach> attachList;
}
