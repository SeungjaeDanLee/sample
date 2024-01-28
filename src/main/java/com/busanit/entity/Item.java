package com.busanit.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
@Getter
@Setter
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String itemNm;
    private int price;              // 가격
    private int stockNumber;        // 재고수량
    private String itemDetail;      // 상품 상세 설명

    @ManyToMany
    @JoinTable(
            name = "member_item",
            joinColumns= @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<Member> member;
}



