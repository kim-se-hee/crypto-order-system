package ksh.example.mybit.member.dto.request;

import ksh.example.mybit.member.service.dto.request.MemberJoinServiceRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class MemberJoinReqeustTest {

    @DisplayName("내용이 같은 서비스 요청 DTO로 변환할 수 있다.")
    @Test
    void toServiceRequest() {
        //given
        String email = "email";
        String password = "password";
        String name = "name";

        MemberJoinRequest request = new MemberJoinRequest();
        request.setEmail(email);
        request.setPassword(password);
        request.setName(name);

        //when
        MemberJoinServiceRequest serviceRequest = request.toServiceRequest();

        //then
        assertThat(serviceRequest)
                .extracting("email", "password", "name")
                .containsExactly(email, password, name);
     }

}
