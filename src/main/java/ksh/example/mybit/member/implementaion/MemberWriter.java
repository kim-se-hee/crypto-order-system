package ksh.example.mybit.member.implementaion;

import ksh.example.mybit.member.domain.Member;
import ksh.example.mybit.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberWriter {

    private final MemberRepository memberRepository;

    public Member create(String email, String password, String name) {
        Member member = new Member(email, password, name);
        return memberRepository.save(member);
    }
}
