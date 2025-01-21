package ksh.example.mybit.member.service;

import ksh.example.mybit.member.domain.Member;
import ksh.example.mybit.member.dto.request.MemberJoinReqeustDto;
import ksh.example.mybit.member.dto.response.MemberJoinResponseDto;
import ksh.example.mybit.member.implementaion.MemberValidator;
import ksh.example.mybit.member.implementaion.MemberWriter;
import ksh.example.mybit.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberValidator memberValidator;
    private final MemberWriter memberWriter;

    public MemberJoinResponseDto join(MemberJoinReqeustDto reqeustDto) {
        memberValidator.checkEmailIsNotDuplicated(reqeustDto.getEmail());

        Member member = memberWriter.create(reqeustDto.getEmail(), reqeustDto.getPassword(), reqeustDto.getName());
        return new MemberJoinResponseDto(member.getId());
    }

    public Member findMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 회원입니다."));
    }
}
