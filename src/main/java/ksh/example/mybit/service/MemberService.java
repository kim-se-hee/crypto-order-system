package ksh.example.mybit.service;

import ksh.example.mybit.domain.Member;
import ksh.example.mybit.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member join(String email, String password, String name) {
        Member findMember = memberRepository.findByEmail(email);
        if(findMember != null) {
            throw new IllegalArgumentException("해당 이메일은 사용할 수 없습니다.");
        }

        Member member = new Member(email, password, name);
        memberRepository.save(member);

        return member;
    }

    public Member findMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 회원입니다."));
    }
}
