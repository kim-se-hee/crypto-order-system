package ksh.example.mybit.member.service;

import ksh.example.mybit.member.domain.Member;
import ksh.example.mybit.member.implementaion.MemberReader;
import ksh.example.mybit.member.service.dto.request.MemberJoinServiceRequest;
import ksh.example.mybit.member.service.dto.response.MemberJoinResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberReader memberReader;

    @DisplayName("신규 회원을 등록한다.")
    @Test
    void join(){
        //given
        MemberJoinServiceRequest request = MemberJoinServiceRequest.builder()
                .name("kim")
                .email("email")
                .password("123456")
                .build();

        //when
        MemberJoinResponse response = memberService.join(request);

        //then
        assertThat(response.getId()).isNotNull();
        Member member = memberReader.readById(response.getId());
        assertThat(member)
                .extracting("id", "name", "email", "password")
                .containsExactly(response.getId(), "kim", "email", "123456");
    }

}
