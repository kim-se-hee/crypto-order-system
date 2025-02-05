package ksh.example.mybit.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import ksh.example.mybit.member.service.dto.request.MemberJoinServiceRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberJoinRequest {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 6)
    private String password;

    @NotBlank
    private String name;

    public MemberJoinServiceRequest toServiceRequest() {
        return MemberJoinServiceRequest.builder()
                .email(email)
                .password(password)
                .name(name)
                .build();
    }
}
