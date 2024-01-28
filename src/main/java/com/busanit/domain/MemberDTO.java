package com.busanit.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MemberDTO {
    private String userId;
    private String userPw;
    private String userName;
    private String userEmail;
    private LocalDateTime userRegDate;
}




