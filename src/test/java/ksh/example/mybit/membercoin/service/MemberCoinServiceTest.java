package ksh.example.mybit.membercoin.service;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import ksh.example.mybit.coin.domain.Coin;
import ksh.example.mybit.coin.repository.CoinRepository;
import ksh.example.mybit.member.domain.Member;
import ksh.example.mybit.member.repository.MemberRepository;
import ksh.example.mybit.membercoin.domain.MemberCoin;
import ksh.example.mybit.membercoin.repository.MemberCoinRepository;
import ksh.example.mybit.membercoin.service.dto.request.FundTransferServiceRequest;
import ksh.example.mybit.membercoin.service.dto.request.InvestmentStaticsServiceRequest;
import ksh.example.mybit.membercoin.service.dto.response.InvestmentStaticsResponse;
import ksh.example.mybit.membercoin.service.dto.response.WalletAssetListResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberCoinServiceTest {

    private static Member member;
    private static Coin coin;

    @Autowired
    private MemberCoinService memberCoinService;

    @Autowired
    private MemberCoinRepository memberCoinRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CoinRepository coinRepository;

    @Autowired
    EntityManager em;

    @PostConstruct
    public void init() {
        member = new Member();
        memberRepository.save(member);

        coin = new Coin();
        coinRepository.save(coin);
    }

    @DisplayName("요청한 금액만큼 계좌에 입금한다")
    @Test
    void deposit1(){
        //given
        MemberCoin memberCoin = createMemberCoin(BigDecimal.valueOf(10000));
        memberCoinRepository.save(memberCoin);

        FundTransferServiceRequest request = FundTransferServiceRequest.builder()
                .coinId(coin.getId())
                .memberId(member.getId())
                .quantity(new BigDecimal(1000))
                .build();

        //when
        memberCoinService.deposit(request);

        //then
        assertThat(memberCoin.getQuantity()).isEqualByComparingTo(BigDecimal.valueOf(11000));

    }

    @DisplayName("요청한 금액만큼 계좌에서 출금한다")
    @Test
    void withdraw1(){
        //given
        MemberCoin memberCoin = createMemberCoin(BigDecimal.valueOf(10000));
        memberCoinRepository.save(memberCoin);

        FundTransferServiceRequest request = FundTransferServiceRequest.builder()
                .coinId(coin.getId())
                .memberId(member.getId())
                .quantity(new BigDecimal(10000))
                .build();

        //when
        memberCoinService.withdraw(request);

        //then
        assertThat(memberCoin.getQuantity()).isEqualByComparingTo(BigDecimal.valueOf(0));

    }

    @DisplayName("자산 보유량 보다 출금 요청액이 크면 예외가 발생한다")
    @Test
    void withdraw2(){
        //given
        MemberCoin memberCoin = createMemberCoin(BigDecimal.valueOf(10000));
        memberCoinRepository.save(memberCoin);

        FundTransferServiceRequest request = FundTransferServiceRequest.builder()
                .coinId(coin.getId())
                .memberId(member.getId())
                .quantity(new BigDecimal(10000.1))
                .build();

        //when //then
        assertThatThrownBy(() -> memberCoinService.withdraw(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("보유 수량이 부족합니다");

    }

    @DisplayName("지갑 내 모든 자산을 조회한다")
    @Test
    void findAllCoinsInWallet(){
        //given
        Coin coin1 = createCoin("btc", BigDecimal.valueOf(10000));
        Coin coin2 = createCoin("eth", BigDecimal.valueOf(1000));
        Coin coin3 = createCoin("sol", BigDecimal.valueOf(100));
        Coin coin4 = createCoin("won", BigDecimal.valueOf(1));
        coinRepository.saveAll(List.of(coin1, coin2, coin3, coin4));

        MemberCoin memberCoin1 = createMemberCoin(coin1, BigDecimal.valueOf(2));
        MemberCoin memberCoin2 = createMemberCoin(coin2, BigDecimal.valueOf(2));
        MemberCoin memberCoin3 = createMemberCoin(coin3, BigDecimal.valueOf(2));
        MemberCoin memberCoin4 = createMemberCoin(coin4, BigDecimal.valueOf(10000));
        memberCoinRepository.saveAll(List.of(memberCoin1, memberCoin2, memberCoin3, memberCoin4));

        //when
        WalletAssetListResponse response = memberCoinService.findAllCoinsInWallet(member.getId());

        //then
        assertThat(response.getWalletAssets()).hasSize(4)
                .extracting("name", "balance", "quantity")
                .containsExactlyInAnyOrder(
                        tuple("btc", BigDecimal.valueOf(20000), BigDecimal.valueOf(2)),
                        tuple("eth", BigDecimal.valueOf(2000), BigDecimal.valueOf(2)),
                        tuple("sol", BigDecimal.valueOf(200), BigDecimal.valueOf(2)),
                        tuple("won", BigDecimal.valueOf(10000), BigDecimal.valueOf(10000))
                );
     }

     @DisplayName("회원이 보유한 자산의 투자 현황을 조회한다")
     @Test
     void getInvestmentStatics(){
         //given
         Coin coin = createCoin("avax", BigDecimal.valueOf(1.24));
         coinRepository.save(coin);

         MemberCoin memberCoin = MemberCoin.builder()
                 .member(member)
                 .coin(coin)
                 .quantity(BigDecimal.valueOf(62.285))
                 .averagePrice(BigDecimal.valueOf(1.17))
                 .build();
         memberCoinRepository.save(memberCoin);

         InvestmentStaticsServiceRequest request = InvestmentStaticsServiceRequest.builder()
                 .coinId(coin.getId())
                 .memberId(member.getId())
                 .build();

         //when
         InvestmentStaticsResponse response = memberCoinService.getInvestmentStatic(request);

         //then
         assertThat(response)
                 .extracting("averagePrice", "quantity", "balance", "roi")
                 .containsExactlyInAnyOrder(BigDecimal.valueOf(1.17), BigDecimal.valueOf(62.285), 77.2334, 5.98);
      }

    private static Coin createCoin(String name, BigDecimal price) {
        return Coin.builder()
                .name(name)
                .price(price)
                .build();
    }

    private static MemberCoin createMemberCoin(BigDecimal quantity){
        return MemberCoin.builder()
                .member(member)
                .coin(coin)
                .quantity(quantity)
                .build();
     }

    private static MemberCoin createMemberCoin(Coin coin, BigDecimal quantity){
        return MemberCoin.builder()
                .member(member)
                .coin(coin)
                .quantity(quantity)
                .build();
    }

}
