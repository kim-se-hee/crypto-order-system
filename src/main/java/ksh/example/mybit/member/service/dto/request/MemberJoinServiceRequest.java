package ksh.example.mybit.member.service.dto.request;

import ksh.example.mybit.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberJoinServiceRequest {
    private String email;
    private String password;
    private String name;

    @Builder
    private MemberJoinServiceRequest(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .password(password)
                .name(name)
                .build();
    }
}
