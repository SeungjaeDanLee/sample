package com.busanit.service;

import com.busanit.constant.Role;
import com.busanit.domain.MemberFormDto;
import com.busanit.domain.MemberSecurityDTO;
import com.busanit.entity.Member;
import com.busanit.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Service
@Log4j2
@AllArgsConstructor
public class CustomOAuth2UserDetailsService extends DefaultOAuth2UserService {

    private MemberRepository memberRepository;
    private PasswordEncoder passwordEncoder;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("oauth2 user....................");

        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        String clientName = clientRegistration.getClientName();

        log.info("clientName: " + clientName);

        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> paramMap = oAuth2User.getAttributes();
        paramMap.forEach((k, v) -> {
            log.info("------------------------");
            log.info(k + " : " + v);
        });

        String email = null;
        if (clientName.equals("Google")) {  // 구글을 이용하는 경우
            email = getGoogleEmail(paramMap);
        } else if(clientName.equals("naver")) {
            email = getNaverEmail(paramMap);
        }
        log.info("email: " + email);

        return generateDTO(email, paramMap);
    }

    private String getNaverEmail(Map<String, Object> paramMap) {
        log.info("Naver--------------------");
        Map<String, Object> resultMap = (Map<String, Object>)paramMap.get("response");
        String email = (String) resultMap.get("email");

        log.info("email : " + email);

        return email;
    }

    private String getGoogleEmail(Map<String, Object> paramMap) {
        log.info("Google-------------------");

        String email = (String) paramMap.get("email");
        log.info("email : " + email);

        return email;
    }

    private MemberSecurityDTO generateDTO(String email, Map<String, Object> paramMap) {
        Optional<Member> result = memberRepository.findByEmail(email);

        // DB에 해당 이메일의 사용자가 없다면 자동으로 회원 가입 처리
        if (result.isEmpty()) {
            // 회원 추가
            // id = 이메일 주소 / 패스워드는 1111
            Member member = Member.builder()
                    .name(email)
                    .password(passwordEncoder.encode("1111"))
                    .email(email)
                    .social(true)
                    .role(Role.USER)
                    .build();

            // DB에 회원 정보 저장(회원 가입 처리)
            memberRepository.save(member);

            MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(email,
                    "1111", email, true,
                    Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
            memberSecurityDTO.setAttr(paramMap);

            return memberSecurityDTO;
        } else {    // 이미 가입된 회원은 기존 정보를 반환
            Member member = result.get();
            MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(
                    member.getName(), member.getPassword(), member.getEmail(),
                    member.isSocial(),
                    Arrays.asList(new SimpleGrantedAuthority("ROLE_" + member.getRole())));

            return memberSecurityDTO;
        }
    }

    public void updatePassword(String password, String email) {
        log.info("update Password----------------");
        log.info("password : " + password);

        memberRepository.updatePassword(password, email);
    }
}
