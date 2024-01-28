package com.busanit.domain;

import com.busanit.constant.Role;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
@Setter
@ToString
public class MemberSecurityDTO extends User implements OAuth2User {
    private String id;
    private String email;
    private String password;
    private String address;
    private boolean social;
    private Role role;
    private Map<String, Object> attr;   // 소설 로그인 정보
    public MemberSecurityDTO(String username, String password, String email,
                             boolean social,
                             Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);

        this.id = username;
        this.password = password;
        this.email = email;
        this.social = social;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.getAttr();
    }

    @Override
    public String getName() {
        return this.id;
    }
}
