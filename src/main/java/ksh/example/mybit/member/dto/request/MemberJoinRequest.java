package ksh.example.mybit.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import ksh.example.mybit.member.service.dto.request.MemberJoinServiceRequest;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberJoinRequest {

    @NotBlank(message = "이메일은 필수입니다")
    @Email
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다")
    @Size(min = 6)
    private String password;

    @NotBlank(message = "회원 이름은 필수입니다")
    private String name;

    public MemberJoinServiceRequest toServiceRequest() {
        return MemberJoinServiceRequest.builder()
                .email(email)
                .password(password)
                .name(name)
                .build();
    }

    @Builder
    private MemberJoinRequest(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

}
