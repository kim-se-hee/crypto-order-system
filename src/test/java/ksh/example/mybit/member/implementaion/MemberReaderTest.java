package ksh.example.mybit.member.implementaion;

import ksh.example.mybit.member.domain.Member;
import ksh.example.mybit.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class MemberReaderTest {

    @Autowired
    private MemberReader memberReader;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("주어진 id를 갖는 회원을 조회할 수 있다")
    @Test
    void readById1(){
        //given
        Member member = Member.builder()
                .name("kim")
                .build();

        memberRepository.save(member);

        //when
        Member findMember = memberReader.readById(member.getId());

        //then
        assertThat(findMember).isEqualTo(member);
     }

    @DisplayName("id에 해당하는 회원이 없다면 예외가 발생한다")
    @Test
    void readById2(){
        //when //then
        assertThatThrownBy(() -> memberReader.readById(2l))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("등록되지 않은 회원입니다.");
    }
}
