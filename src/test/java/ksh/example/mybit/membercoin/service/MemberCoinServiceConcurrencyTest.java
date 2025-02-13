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
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Commit
@Slf4j
public class MemberCoinServiceConcurrencyTest {

    private Member member;
    private Coin coin;
    private MemberCoin memberCoin;

    @Autowired
    private MemberCoinService memberCoinService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CoinRepository coinRepository;

    @Autowired
    private MemberCoinRepository memberCoinRepository;

    @Autowired
    private EntityManager em;


    @PostConstruct
    public void init() {
        member = new Member();
        memberRepository.save(member);

        coin = new Coin();
        coinRepository.save(coin);

        memberCoin = MemberCoin.builder()
                .member(member)
                .coin(coin)
                .quantity(BigDecimal.valueOf(10000))
                .build();
        memberCoinRepository.save(memberCoin);
    }

    @DisplayName("한 자산에 대한 입출금이 동시에 요청돼도 각각 처리된다")
    @Test
    void concurrentDepositWithdraw1() throws InterruptedException {
        //given
        FundTransferServiceRequest depositRequest = FundTransferServiceRequest.builder()
                .coinId(coin.getId())
                .memberId(member.getId())
                .quantity(new BigDecimal(1000))
                .build();

        FundTransferServiceRequest withdrawRequest = FundTransferServiceRequest.builder()
                .coinId(coin.getId())
                .memberId(member.getId())
                .quantity(new BigDecimal(10000))
                .build();



        CountDownLatch countDownLatch = new CountDownLatch(2);
        Thread depositThread = new Thread(() -> {
            memberCoinService.deposit(depositRequest);
            countDownLatch.countDown();
        });
        Thread withdrawThread = new Thread(() -> {
            memberCoinService.withdraw(withdrawRequest);
            countDownLatch.countDown();
        });

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        //when
        executorService.execute(depositThread);
        executorService.execute(withdrawThread);
        countDownLatch.await();

        //then
        MemberCoin updatedMemberCoin = memberCoinRepository.findById(member.getId()).get();
        assertThat(updatedMemberCoin.getQuantity().intValue()).isEqualTo(1000);
        em.createNativeQuery("update member_coin set quantity = 10000 where member_coin_id = 1").executeUpdate();
    }

    @DisplayName("입출금이 동시에 요청됐을 때 출금이 실패하여도 입금은 진행된다")
    @Test
    void concurrentDepositWithdraw2() throws InterruptedException {
        //given
        FundTransferServiceRequest depositRequest = FundTransferServiceRequest.builder()
                .coinId(coin.getId())
                .memberId(member.getId())
                .quantity(new BigDecimal(1000))
                .build();

        FundTransferServiceRequest withdrawRequest = FundTransferServiceRequest.builder()
                .coinId(coin.getId())
                .memberId(member.getId())
                .quantity(new BigDecimal(20000))
                .build();



        CountDownLatch countDownLatch = new CountDownLatch(2);
        Thread depositThread = new Thread(() -> {
            memberCoinService.deposit(depositRequest);
            countDownLatch.countDown();
        });
        Thread withdrawThread = new Thread(() -> {
            try {
                memberCoinService.withdraw(withdrawRequest);
            } catch (Exception e) {
                log.info(e.getMessage());
            } finally {
                countDownLatch.countDown();
            }

        });

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        //when
        executorService.execute(depositThread);
        executorService.execute(withdrawThread);
        countDownLatch.await();

        //then
        MemberCoin updatedMemberCoin = memberCoinRepository.findById(member.getId()).get();
        assertThat(updatedMemberCoin.getQuantity().intValue()).isEqualTo(11000);
        em.createNativeQuery("update member_coin set quantity = 10000 where member_coin_id = 1").executeUpdate();
    }

}
