package ksh.example.mybit.membercoin.implementation;

import ksh.example.mybit.coin.domain.Coin;
import ksh.example.mybit.coin.repository.CoinRepository;
import ksh.example.mybit.member.domain.Member;
import ksh.example.mybit.member.repository.MemberRepository;
import ksh.example.mybit.membercoin.domain.MemberCoin;
import ksh.example.mybit.membercoin.repository.MemberCoinRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class WalletReaderTest {

    @Autowired
    private WalletReader walletReader;

    @Autowired
    private MemberCoinRepository memberCoinRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CoinRepository coinRepository;

    @DisplayName("회원의 지갑 내 자산 보유 현황을 락을 걸어 조회한다")
    @Test
    void readMemberCoinWithLock1() {
        //given
        Member member = Member.builder()
                .build();
        memberRepository.save(member);

        Coin coin = Coin.builder()
                .build();
        coinRepository.save(coin);

        MemberCoin memberCoin = MemberCoin.builder()
                .member(member)
                .coin(coin)
                .build();
        memberCoinRepository.save(memberCoin);

        //when
        MemberCoin findMemberCoin = walletReader.readMemberCoinWithLock(member.getId(), coin.getId());

        //then
        assertThat(findMemberCoin).isEqualTo(memberCoin);

     }

    @DisplayName("락을 걸어 회원의 지갑 내 자산을 조회시 보유하지 않은 자산이라면 예외가 발생한다")
    @Test
    void readMemberCoinWithLock2() {

        //when //then
        assertThatThrownBy(() -> walletReader.readMemberCoinWithLock(2l, 2l))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 자산을 보유하고 있지 않습니다");

    }

    @DisplayName("회원의 지갑 내 모든 자산을 조회합니다.")
    @Test
    void readAllCoinsOfMember1(){
        //given
        Member member = Member.builder()
                .build();
        memberRepository.save(member);

        Coin coin1 = Coin.builder()
                .name("btc")
                .build();

        Coin coin2 = Coin.builder()
                .name("eth")
                .build();

        Coin coin3 = Coin.builder()
                .name("sol")
                .build();
        coinRepository.saveAll(List.of(coin1, coin2, coin3));

        MemberCoin memberCoin1 = MemberCoin.builder()
                .member(member)
                .coin(coin1)
                .build();

        MemberCoin memberCoin2 = MemberCoin.builder()
                .member(member)
                .coin(coin2)
                .build();

        MemberCoin memberCoin3 = MemberCoin.builder()
                .member(member)
                .coin(coin3)
                .build();
        memberCoinRepository.saveAll(List.of(memberCoin1, memberCoin2, memberCoin3));

        //when
        List<MemberCoin> memberCoins = walletReader.readAllCoinOfMember(member.getId());

        //then
        assertThat(memberCoins).hasSize(3)
                .extracting("coin.name")
                .containsExactlyInAnyOrder("btc", "eth", "sol");

     }

     @DisplayName("회원의 모든 보유 자산 조회 시 보유 중인 자산이 없다면 빈 리스트가 조회된다")
     @Test
     void readAllCoinsOfMember2(){
         //when
         List<MemberCoin> memberCoins = walletReader.readAllCoinOfMember(1l);

         //then
         assertThat(memberCoins).isEmpty();

     }

    @DisplayName("주어진 회원 id와 코인 id로 회원의 지갑 내 자산 보유 현황을 조회한다")
    @Test
    void readByMemberIdAndCoinId1() {
        //given
        Member member = Member.builder()
                .build();
        memberRepository.save(member);

        Coin coin = Coin.builder()
                .build();
        coinRepository.save(coin);

        MemberCoin memberCoin = MemberCoin.builder()
                .member(member)
                .coin(coin)
                .build();
        memberCoinRepository.save(memberCoin);

        //when
        MemberCoin findMemberCoin = walletReader.readByMemberIdAndCoinId(member.getId(), coin.getId());

        //then
        assertThat(findMemberCoin).isEqualTo(memberCoin);

    }

    @DisplayName("회원의 지갑 내 자산을 조회시 보유하지 않은 자산이라면 예외가 발생한다")
    @Test
    void readByMemberIdAndCoinId2() {

        //when //then
        assertThatThrownBy(() -> walletReader.readByMemberIdAndCoinId(2l, 2l))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 자산을 보유하고 있지 않습니다");

    }

    @DisplayName("주어진 회원 id와 티커로 회원의 지갑 내 자산 보유 현황을 조회한다")
    @Test
    void readByMemberIdAndTicker1() {
        //given
        Member member = Member.builder()
                .build();
        memberRepository.save(member);

        Coin coin = Coin.builder()
                .ticker("btc")
                .build();
        coinRepository.save(coin);

        MemberCoin memberCoin = MemberCoin.builder()
                .member(member)
                .coin(coin)
                .build();
        memberCoinRepository.save(memberCoin);

        //when
        MemberCoin findMemberCoin = walletReader.readByMemberIdAndTicker(member.getId(), "btc");

        //then
        assertThat(findMemberCoin).isEqualTo(memberCoin);

    }

    @DisplayName("티커로 회원의 지갑 내 자산을 조회시 보유하지 않은 자산이라면 예외가 발생한다")
    @Test
    void readByMemberIdAndTicker2() {

        //when //then
        assertThatThrownBy(() -> walletReader.readByMemberIdAndTicker(1l, "eth"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 자산을 보유하고 있지 않습니다");

    }

}
