package com.busanit.handler;

import com.busanit.domain.MemberSecurityDTO;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
@AllArgsConstructor
public class CustomSocialLoginSuccessHandler implements AuthenticationSuccessHandler {
    private PasswordEncoder passwordEncoder;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("-------------------------------------------");
        log.info(authentication.getPrincipal());

        MemberSecurityDTO memberSecurityDTO =
                (MemberSecurityDTO) authentication.getPrincipal();

        String encodePassword = memberSecurityDTO.getPassword();

        // 소셜 로그인이고 회원의 패스워드가 1111이면
        if (memberSecurityDTO.isSocial()
                && memberSecurityDTO.getPassword().equals("1111")
                || passwordEncoder.matches("1111",
                                    memberSecurityDTO.getPassword())) {
            log.info("비밀번호 변경해야함!!!");

            response.sendRedirect("/members/modify");
            return;
        } else {
            response.sendRedirect("/");
        }
    }
}





