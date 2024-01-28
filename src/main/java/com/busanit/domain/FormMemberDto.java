package com.busanit.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
@Setter
@ToString
public class FormMemberDto extends User implements UserDetails {

    private String email;
    private String password;
    private String name;
    private boolean social;
    private Map<String, Object> attr;

    public FormMemberDto(
            String username,
            String password,
            boolean social,
            Collection<? extends GrantedAuthority> authorities,
            Map<String, Object> attr) {
        this(username, password, social, authorities);
        this.attr = attr;
    }

    public FormMemberDto(
            String username,
            String password,
            boolean social,
            Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.email = username;
        this.password = password;
        this.social = social;
    }

}
