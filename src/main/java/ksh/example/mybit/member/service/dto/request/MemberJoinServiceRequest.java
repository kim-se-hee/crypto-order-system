package ksh.example.mybit.member.service.dto.request;

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
}
