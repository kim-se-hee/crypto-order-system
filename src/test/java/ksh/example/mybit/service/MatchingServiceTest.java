package ksh.example.mybit.service;

import ksh.example.mybit.persistence.mysql.jpa.entity.*;
import ksh.example.mybit.persistence.mysql.jpa.entity.type.OrderSide;
import ksh.example.mybit.persistence.mysql.jpa.entity.type.OrderType;
import ksh.example.mybit.persistence.mysql.jpa.repository.CoinRepository;
import ksh.example.mybit.persistence.mysql.jpa.repository.MemberCoinRepository;
import ksh.example.mybit.persistence.mysql.jpa.repository.MemberRepository;
import ksh.example.mybit.persistence.mysql.jpa.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MatchingServiceTest {

    @Autowired
    MatchingService matchingService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    CoinRepository coinRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    MemberCoinRepository memberCoinRepository;

    @Test
    public void 지정가_지정가_매칭() throws Exception {
        Member member1 = memberRepository.findById(1L).get();
        Member member2 = memberRepository.findById(2L).get();
        Coin btc = coinRepository.findById(1L).get();

        Order sellOrder = new Order(1000, OrderSide.SELL, OrderType.LIMIT, new BigDecimal(62000), member1, btc);
        Order buyOrder = new Order(500, OrderSide.BUY, OrderType.LIMIT, new BigDecimal(62000), member2, btc);
        orderRepository.save(sellOrder);
        orderRepository.save(buyOrder);

        Trade trade = matchingService.matchOrder();
        assertThat(trade.getExecutedAmount()).isEqualTo(500L);
        assertThat(trade.getSellOrder().getId()).isEqualTo(sellOrder.getId());
        assertThat(trade.getBuyOrder().getId()).isEqualTo(buyOrder.getId());

        MemberCoin memberCoin1 = memberCoinRepository.findByMemberAndCoin(member1, btc).get();
        assertThat(memberCoin1.getKoreanWonValue()).isEqualTo(9500L);

        MemberCoin memberCoin2 = memberCoinRepository.findByMemberAndCoin(member2, btc).get();
        assertThat(memberCoin2.getKoreanWonValue()).isEqualTo(2500L);
    }

    @Test
    public void 지정가_지정가_비매칭() throws Exception {
        Member member1 = memberRepository.findById(1L).get();
        Member member2 = memberRepository.findById(2L).get();
        Coin btc = coinRepository.findById(1L).get();

        Order sellOrder = new Order(1000, OrderSide.SELL, OrderType.LIMIT, new BigDecimal(62000), member1, btc);
        Order buyOrder = new Order(500, OrderSide.BUY, OrderType.LIMIT, new BigDecimal(61000), member2, btc);
        orderRepository.save(sellOrder);
        orderRepository.save(buyOrder);

        Assertions.assertThrows(NoSuchElementException.class, () -> matchingService.matchOrder());
    }

    @Test
    public void 비매칭_다른_코인() throws Exception {
        Member member1 = memberRepository.findById(1L).get();
        Member member2 = memberRepository.findById(2L).get();
        Coin btc = coinRepository.findById(1L).get();
        Coin eth = coinRepository.findById(2L).get();

        Order sellOrder = new Order(1000, OrderSide.SELL, OrderType.LIMIT, new BigDecimal(62000), member1, btc);
        Order buyOrder = new Order(500, OrderSide.BUY, OrderType.LIMIT, new BigDecimal(61000), member2, eth);
        orderRepository.save(sellOrder);
        orderRepository.save(buyOrder);

        Assertions.assertThrows(NoSuchElementException.class, () -> matchingService.matchOrder());
    }

    @Test
    public void 지정가_시장가_매칭() throws Exception {
        Member member1 = memberRepository.findById(1L).get();
        Member member2 = memberRepository.findById(2L).get();
        Coin btc = coinRepository.findById(1L).get();

        Order sellOrder = new Order(1000, OrderSide.SELL, OrderType.LIMIT, new BigDecimal(62000), member1, btc);
        Order buyOrder = new Order(500, OrderSide.BUY, OrderType.MARKET, null, member2, btc);
        orderRepository.save(sellOrder);
        orderRepository.save(buyOrder);

        Trade trade = matchingService.matchOrder();
        assertThat(trade.getExecutedAmount()).isEqualTo(500L);
        assertThat(trade.getSellOrder().getId()).isEqualTo(sellOrder.getId());
        assertThat(trade.getBuyOrder().getId()).isEqualTo(buyOrder.getId());

        MemberCoin memberCoin1 = memberCoinRepository.findByMemberAndCoin(member1, btc).get();
        assertThat(memberCoin1.getKoreanWonValue()).isEqualTo(9500L);

        MemberCoin memberCoin2 = memberCoinRepository.findByMemberAndCoin(member2, btc).get();
        assertThat(memberCoin2.getKoreanWonValue()).isEqualTo(2500L);
    }

    @Test
    public void 둘_다_지정가_비싼_게_먼저_매칭() throws Exception {
        Member member1 = memberRepository.findById(1L).get();
        Member member2 = memberRepository.findById(2L).get();
        Coin btc = coinRepository.findById(1L).get();

        Order sellOrder = new Order(1000, OrderSide.SELL, OrderType.MARKET, null, member1, btc);
        Order buyOrder1 = new Order(400, OrderSide.BUY, OrderType.LIMIT, new BigDecimal(62000), member2, btc);
        Order buyOrder2 = new Order(600, OrderSide.BUY, OrderType.LIMIT, new BigDecimal(65000), member2, btc);
        orderRepository.save(sellOrder);
        orderRepository.save(buyOrder1);
        orderRepository.save(buyOrder2);

        Trade trade = matchingService.matchOrder();
        assertThat(trade.getExecutedAmount()).isEqualTo(600L);
        assertThat(trade.getSellOrder().getId()).isEqualTo(sellOrder.getId());
        assertThat(trade.getBuyOrder().getId()).isEqualTo(buyOrder2.getId());

        MemberCoin memberCoin1 = memberCoinRepository.findByMemberAndCoin(member1, btc).get();
        assertThat(memberCoin1.getKoreanWonValue()).isEqualTo(9400L);

        MemberCoin memberCoin2 = memberCoinRepository.findByMemberAndCoin(member2, btc).get();
        assertThat(memberCoin2.getKoreanWonValue()).isEqualTo(2600L);
    }

    @Test
    public void 둘_다_지정가_지정가가_같아서_빠른_게_먼저_매칭() throws Exception {
        Member member1 = memberRepository.findById(1L).get();
        Member member2 = memberRepository.findById(2L).get();
        Coin btc = coinRepository.findById(1L).get();

        Order sellOrder = new Order(1000, OrderSide.SELL, OrderType.MARKET, null, member1, btc);
        Order buyOrder1 = new Order(400, OrderSide.BUY, OrderType.LIMIT, new BigDecimal(65000), member2, btc);
        Order buyOrder2 = new Order(600, OrderSide.BUY, OrderType.LIMIT, new BigDecimal(65000), member2, btc);
        orderRepository.save(sellOrder);
        orderRepository.save(buyOrder1);
        orderRepository.save(buyOrder2);

        Trade trade = matchingService.matchOrder();
        assertThat(trade.getExecutedAmount()).isEqualTo(400L);
        assertThat(trade.getSellOrder().getId()).isEqualTo(sellOrder.getId());
        assertThat(trade.getBuyOrder().getId()).isEqualTo(buyOrder1.getId());

        MemberCoin memberCoin1 = memberCoinRepository.findByMemberAndCoin(member1, btc).get();
        assertThat(memberCoin1.getKoreanWonValue()).isEqualTo(9600L);

        MemberCoin memberCoin2 = memberCoinRepository.findByMemberAndCoin(member2, btc).get();
        assertThat(memberCoin2.getKoreanWonValue()).isEqualTo(2400L);
    }

}
