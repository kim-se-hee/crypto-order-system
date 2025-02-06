package ksh.example.mybit.member.service.dto.request;

import ksh.example.mybit.member.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MemberJoinServiceRequestTest {

    @DisplayName("요청 정보를 바탕으로 회원을 생성한다")
    @Test
    void toEntity(){
        //given
        String email = "test@test.com";
        String password = "123456";
        String name = "test";
        MemberJoinServiceRequest request = MemberJoinServiceRequest.builder()
                .email(email)
                .password(password)
                .name(name)
                .build();

        //when
        Member member = request.toEntity();

        //then
        assertThat(member)
                .extracting("email", "password", "name")
                .containsExactly(email, password, name);

     }

}
