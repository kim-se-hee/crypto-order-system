package ksh.example.mybit.repository;

import ksh.example.mybit.domain.Member;
import ksh.example.mybit.domain.MemberCoin;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberCoinRepositoryTest {
    @Autowired MemberCoinRepository memberCoinRepository;
    @Autowired MemberRepository memberRepository;

    @Test
    public void 테스트() throws Exception{
        Member member = memberRepository.findById(1l).get();

    }
}
