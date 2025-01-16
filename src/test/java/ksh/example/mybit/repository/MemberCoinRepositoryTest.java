package ksh.example.mybit.repository;

import ksh.example.mybit.persistence.mysql.jpa.entity.Member;
import ksh.example.mybit.persistence.mysql.jpa.repository.MemberCoinRepository;
import ksh.example.mybit.persistence.mysql.jpa.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MemberCoinRepositoryTest {
    @Autowired
    MemberCoinRepository memberCoinRepository;
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 테스트() throws Exception{
        Member member = memberRepository.findById(1l).get();

    }
}
