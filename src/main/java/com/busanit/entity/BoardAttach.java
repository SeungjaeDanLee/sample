package com.busanit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table
@Getter
@Setter
public class BoardAttach extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long attachNo;
    private String fileName;
    private String thumbnailName;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;
}


