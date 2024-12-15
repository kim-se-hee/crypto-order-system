package ksh.example.mybit.service;

import ksh.example.mybit.domain.Member;
import ksh.example.mybit.implementation.Validator;
import ksh.example.mybit.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final Validator validator;
    public Member join(String email, String password, String name) {
        validator.checkEmailIsAvailable(email);

        Member member = new Member(email, password, name);
        memberRepository.save(member);
        return member;
    }

    public Member findMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 회원입니다."));
    }
}
