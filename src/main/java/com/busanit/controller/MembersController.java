package com.busanit.controller;

import com.busanit.domain.MemberFormDto;
import com.busanit.domain.MemberSecurityDTO;
import com.busanit.entity.Member;
import com.busanit.service.CustomOAuth2UserDetailsService;
import com.busanit.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/members")
@AllArgsConstructor
public class MembersController {

    private MemberService memberService;
    private CustomOAuth2UserDetailsService customOAuth2UserDetailsService;
    private PasswordEncoder passwordEncoder;

    @GetMapping("/new")
    public String memberForm(Model model) {
        model.addAttribute("memberFormDto", new MemberFormDto());

        return "member/memberForm";
    }

    @PostMapping("/new")
    public String memberForm(@Valid MemberFormDto memberFormDto,
                             BindingResult bindingResult, Model model) {

        // 에러가 있으면 회원 가입 페이지로 이동
        if (bindingResult.hasErrors()) {
            return "member/memberForm";
        }

        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberForm";
        }

        return "redirect:/board/list";
    }

    @GetMapping("/login")
    public String loginMember() {
        return "member/memberLoginForm";
    }

    @GetMapping("/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginErrorMsg",
                "아이디 또는 비밀번호를 확인해주세요.");

        return "member/memberLoginForm";
    }

    @GetMapping("/modify")
    public String modify(Model model) {
        model.addAttribute("memberFormDto", new MemberFormDto());

        return "member/memberModifyForm";
    }

    @PostMapping("/modify")
    public String modify(String password,
             @AuthenticationPrincipal MemberSecurityDTO memberSecurityDTO) {
        customOAuth2UserDetailsService
                .updatePassword(passwordEncoder.encode(password),
                        memberSecurityDTO.getEmail());
        return "redirect:/";
    }
}








