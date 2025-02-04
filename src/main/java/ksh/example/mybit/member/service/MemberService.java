package ksh.example.mybit.member.service;

import ksh.example.mybit.member.domain.Member;
import ksh.example.mybit.member.service.dto.request.MemberJoinServiceRequest;
import ksh.example.mybit.member.service.dto.response.MemberJoinResponse;
import ksh.example.mybit.member.implementaion.MemberReader;
import ksh.example.mybit.member.implementaion.MemberValidator;
import ksh.example.mybit.member.implementaion.MemberWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberReader memberReader;
    private final MemberValidator memberValidator;
    private final MemberWriter memberWriter;

    public MemberJoinResponse join(MemberJoinServiceRequest reqeustDto) {
        memberValidator.checkEmailIsNotDuplicated(reqeustDto.getEmail());

        Member member = memberWriter.create(reqeustDto.toEntity());
        return new MemberJoinResponse(member.getId());
    }

    public Long getById(Long id) {
        Member member = memberReader.readById(id);

        return member.getId();
    }
}
