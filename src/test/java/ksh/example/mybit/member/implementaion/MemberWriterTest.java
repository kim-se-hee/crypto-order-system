package ksh.example.mybit.member.implementaion;

import ksh.example.mybit.member.domain.Member;
import ksh.example.mybit.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberWriterTest {

    @Autowired
    private MemberWriter memberWriter;

    @DisplayName("회원을 저장한다")
    @Test
    void join(){
        //given
        Member member = new Member();

        //when
        Member savedMember = memberWriter.join(member);

        //then
        assertThat(savedMember).isEqualTo(member);
        assertThat(savedMember.getId()).isNotNull();
     }
}
