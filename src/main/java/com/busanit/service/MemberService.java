package com.busanit.service;

import com.busanit.domain.FormMemberDto;
import com.busanit.entity.Member;
import com.busanit.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class MemberService implements UserDetailsService {

    private MemberRepository memberRepository;

    public Member saveMember(Member member) {
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member) {
        Optional<Member> findMember = memberRepository.findByEmail(member.getEmail());
        // .isPresent() - Optional 객체가 값을 가지고 있다면 true, 없다면 false
        if (findMember.isPresent()) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Member> member = memberRepository.findByEmail(email);

        if (!member.isPresent()) {
            throw new UsernameNotFoundException(email);
        }

        FormMemberDto dto = new FormMemberDto(
                member.get().getEmail(),
                member.get().getPassword(),
                member.get().isSocial(),
                Arrays.asList(new SimpleGrantedAuthority(
                        "ROLE_" + member.get().getRole()))
        );

        return dto;
//        return User.builder()
//                .username(member.get().getEmail())
//                .password(member.get().getPassword())
//                .roles(member.get().getRole().toString())
//                .build();
    }

}



