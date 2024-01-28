package com.busanit.config;

import com.busanit.handler.CustomFormLoginSuccessHandler;
import com.busanit.handler.CustomSocialLoginSuccessHandler;
import com.busanit.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private MemberService memberService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 페이지 권한 설정, 로그인 페이지 설정, 로그아웃 메소드 등에 대한 설정을 작성
        http.formLogin()
                .loginPage("/members/login")
//                .defaultSuccessUrl("/")
                .successHandler(authenticationFormLoginSuccessHandler())
                .usernameParameter("email")
                .failureUrl("/members/login/error")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
                .logoutSuccessUrl("/")
        ;

        http.oauth2Login()
                .loginPage("/members/login")
//                .defaultSuccessUrl("/")
                .successHandler(authenticationSuccessHandler())
                .failureUrl("/members/login/error")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
                .logoutSuccessUrl("/")
        ;

        /* csrf 전송 끄기 */
//        http.csrf().disable();
        /*
        permitAll() - 모든 사용자가 인증(로그인)없이 해당 경로에 접근할 수 있도록 설정
        anyRequest().authenticated() - mvcMatchers에서 설정해준 경로를 제외한
                                      나머지 경로들은 모두 인증을 요구하도록 설정
         */
        http.authorizeRequests()
            .mvcMatchers("/css/**", "/js/**",
                    "/img/**", "/vendor/**").permitAll()
            .mvcMatchers("/", "/members/**", "/replies/**",
                    "/uploadAjax", "/uploadEx", "/display", "/removeFile").permitAll()
            //.mvcMatchers("/board/**").hasRole("ADMIN")
            .mvcMatchers("/board/**").hasAnyRole("ADMIN", "USER")
            .anyRequest().authenticated()
        ;

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCryptPasswordEncoder의 해시 함수를 이용해서 비밀번호 암호화
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationFormLoginSuccessHandler() {
        return new CustomFormLoginSuccessHandler();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomSocialLoginSuccessHandler(passwordEncoder());
    }


}




