package ksh.example.mybit.member.implementaion;

import ksh.example.mybit.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberValidator {

    private final MemberRepository memberRepository;

    public void checkEmailIsNotDuplicated(String email) {
        memberRepository.findByEmail(email)
                .ifPresent(member -> {
                    throw new IllegalArgumentException("해당 이메일은 사용할 수 없습니다.");
                });
    }

}
