package ksh.example.mybit.trade.repository;

import ksh.example.mybit.coin.domain.Coin;
import ksh.example.mybit.coin.repository.CoinRepository;
import ksh.example.mybit.member.domain.Member;
import ksh.example.mybit.member.repository.MemberRepository;
import ksh.example.mybit.order.domain.Order;
import ksh.example.mybit.order.domain.OrderSide;
import ksh.example.mybit.order.repository.OrderRepository;
import ksh.example.mybit.trade.domain.Trade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

import static ksh.example.mybit.order.domain.OrderSide.BUY;
import static ksh.example.mybit.order.domain.OrderSide.SELL;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class TradeRepositoryTest {

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CoinRepository coinRepository;

    @DisplayName("회원은 자신의 특정 코인 매수 거래 기록을 조회할 수 있다")
    @Test
    void findByMemberIdAndCoinId1() {
        //given
        Member member1 = new Member();
        Member member2 = new Member();
        memberRepository.saveAll(List.of(member1, member2));

        Coin coin = new Coin();
        coinRepository.save(coin);

        Order buyOrder = createOrder(BUY, member1, coin);
        Order sellOrder = createOrder(SELL, member2, coin);

        orderRepository.saveAll(List.of(buyOrder, sellOrder));

        Trade trade = createTrade(buyOrder, sellOrder);
        tradeRepository.save(trade);

        PageRequest pageRequest = PageRequest.of(0, 1);

        //when
        List<Trade> trades = tradeRepository.findByMemberIdAndCoinId(member1.getId(), coin.getId(), pageRequest);

        //then
        assertThat(trades).hasSize(1)
                .extracting("buyOrder")
                .containsExactly(buyOrder);

     }

    @DisplayName("회원은 자신의 특정 코인 매도 거래 기록을 조회할 수 있다")
    @Test
    void findByMemberIdAndCoinId2() {
        //given
        Member member1 = new Member();
        Member member2 = new Member();
        memberRepository.saveAll(List.of(member1, member2));

        Coin coin = new Coin();
        coinRepository.save(coin);

        Order buyOrder = createOrder(BUY, member1, coin);
        Order sellOrder =createOrder(SELL, member2, coin);

        orderRepository.saveAll(List.of(buyOrder, sellOrder));

        Trade trade = createTrade(buyOrder, sellOrder);

        tradeRepository.save(trade);

        PageRequest pageRequest = PageRequest.of(0, 1);

        //when
        List<Trade> trades = tradeRepository.findByMemberIdAndCoinId(member2.getId(), coin.getId(), pageRequest);

        //then
        assertThat(trades).hasSize(1)
                .extracting("sellOrder")
                .containsExactly(sellOrder);

    }

    @DisplayName("회원은 자신의 생성 시간을 기준으로 오름차순 정렬된 특정 코인 거래 기록을 페이징 하여 조회할 수 있다")
    @Test
    void findByMemberIdAndCoinId3() {
        //given
        Member member1 = new Member();
        Member member2 = new Member();
        Member member3 = new Member();
        memberRepository.saveAll(List.of(member1, member2, member3));

        Coin coin1 = new Coin();
        Coin coin2 = new Coin();
        coinRepository.saveAll(List.of(coin1, coin2));

        Order buyOrder1 = createOrder(BUY, member1, coin1);
        Order sellOrder1 = createOrder(SELL, member2, coin1);

        Order buyOrder2 = createOrder(BUY, member2, coin1);
        Order sellOrder2 = createOrder(SELL, member1, coin1);

        Order buyOrder3 = createOrder(BUY, member2, coin2);
        Order sellOrder3 = createOrder(SELL, member3, coin2);

        orderRepository.saveAll(List.of(buyOrder1, buyOrder2, buyOrder3,sellOrder1, sellOrder2, sellOrder3));

        Trade trade1 = createTrade(buyOrder1, sellOrder1);

        Trade trade2 = createTrade(buyOrder2, sellOrder2);

        Trade trade3 = createTrade(buyOrder3, sellOrder3);

        tradeRepository.saveAll(List.of(trade1, trade2, trade3));

        PageRequest pageRequest = PageRequest.of(0, 2);

        //when
        List<Trade> trades = tradeRepository.findByMemberIdAndCoinId(member1.getId(), coin1.getId(), pageRequest);

        //then
        assertThat(trades).hasSize(2);
        assertThat(trades.get(0).getBuyOrder()).isEqualTo(buyOrder1);
        assertThat(trades.get(1).getSellOrder()).isEqualTo(sellOrder2);
        assertThat(trades).isSortedAccordingTo(Comparator.comparing(Trade::getCreatedAt));
    }

    private static Order createOrder(OrderSide orderSide, Member member, Coin coin) {
        return Order.builder()
                .orderSide(orderSide)
                .member(member)
                .coin(coin)
                .build();
    }

    private static Trade createTrade(Order buyOrder, Order sellOrder) {
        return Trade.builder()
                .buyOrder(buyOrder)
                .sellOrder(sellOrder)
                .build();
    }
}
