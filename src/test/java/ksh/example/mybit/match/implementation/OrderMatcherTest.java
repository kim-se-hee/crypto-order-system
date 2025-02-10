package ksh.example.mybit.match.implementation;

import ksh.example.mybit.coin.domain.Coin;
import ksh.example.mybit.coin.repository.CoinRepository;
import ksh.example.mybit.member.domain.Member;
import ksh.example.mybit.member.repository.MemberRepository;
import ksh.example.mybit.order.domain.Order;
import ksh.example.mybit.order.domain.OrderStatus;
import ksh.example.mybit.order.domain.OrderType;
import ksh.example.mybit.order.implementation.OrderReader;
import ksh.example.mybit.order.repository.OrderRepository;
import ksh.example.mybit.trade.domain.Trade;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static ksh.example.mybit.order.domain.OrderSide.BUY;
import static ksh.example.mybit.order.domain.OrderSide.SELL;
import static ksh.example.mybit.order.domain.OrderStatus.*;
import static ksh.example.mybit.order.domain.OrderType.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderMatcherTest {

    @Autowired
    private OrderMatcher orderMatcher;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CoinRepository coinRepository;

    @Autowired
    private MemberRepository memberRepository;


    @DisplayName("시장가 매수와 지정가 매도를 매칭하여 주문을 생성한다")
    @Test
    void match1(){
        //given
        Member member1 = new Member();
        Member member2 = new Member();
        memberRepository.saveAll(List.of(member1, member2));

        Coin coin = Coin.builder()
                .name("btc")
                .price(BigDecimal.valueOf(65000))
                .build();
        coinRepository.save(coin);

        Order buyOrder = Order.builder()
                .orderSide(BUY)
                .orderType(MARKET)
                .orderStatus(PENDING)
                .volume(100000)
                .member(member1)
                .coin(coin)
                .build();

        Order sellOrder = Order.builder()
                .orderSide(SELL)
                .orderType(LIMIT)
                .orderStatus(PENDING)
                .volume(40000)
                .limitPrice(BigDecimal.valueOf(64999))
                .member(member2)
                .coin(coin)
                .build();

        orderRepository.saveAll(List.of(buyOrder, sellOrder));

        //when
        Trade trade = orderMatcher.match(buyOrder, sellOrder);

        //then
        assertThat(trade).extracting("executedQuantity", "executedPrice", "executedVolume")
                .containsExactly(
                        BigDecimal.valueOf(40000).divide(BigDecimal.valueOf(64999), 8, RoundingMode.FLOOR),
                        BigDecimal.valueOf(64999),
                        40000
                );

        assertThat(buyOrder).extracting("orderSide", "volume", "orderStatus")
                .containsExactly(BUY, 60000, PENDING);

        assertThat(sellOrder).extracting("orderSide", "volume", "orderStatus")
                .containsExactly(SELL, 0, FINISHED);
     }

    @DisplayName("시장가 매도와 지정가 매수를 매칭하여 주문을 생성한다")
    @Test
    void match2(){
        //given
        Member member1 = new Member();
        Member member2 = new Member();
        memberRepository.saveAll(List.of(member1, member2));

        Coin coin = Coin.builder()
                .name("btc")
                .price(BigDecimal.valueOf(65000))
                .build();
        coinRepository.save(coin);

        Order sellOrder = Order.builder()
                .orderSide(SELL)
                .orderType(MARKET)
                .orderStatus(PENDING)
                .volume(100000)
                .member(member1)
                .coin(coin)
                .build();

        Order buyOrder = Order.builder()
                .orderSide(BUY)
                .orderType(LIMIT)
                .orderStatus(PENDING)
                .volume(40000)
                .limitPrice(BigDecimal.valueOf(64999))
                .member(member2)
                .coin(coin)
                .build();

        orderRepository.saveAll(List.of(buyOrder, sellOrder));

        //when
        Trade trade = orderMatcher.match(sellOrder, buyOrder);

        //then
        assertThat(trade).extracting("executedQuantity", "executedPrice", "executedVolume")
                .containsExactly(
                        BigDecimal.valueOf(40000).divide(BigDecimal.valueOf(64999), 8, RoundingMode.FLOOR),
                        BigDecimal.valueOf(64999),
                        40000
                );
        assertThat(buyOrder).extracting("orderSide", "volume", "orderStatus")
                        .containsExactly(BUY, 0, FINISHED);

        assertThat(sellOrder).extracting("orderSide", "volume", "orderStatus")
                .containsExactly(SELL, 60000, PENDING);
    }

    @DisplayName("지정가 매수와 지정가 매도를 매칭하여 주문을 생성한다")
    @Test
    void match3(){
        //given
        Member member1 = new Member();
        Member member2 = new Member();
        memberRepository.saveAll(List.of(member1, member2));

        Coin coin = Coin.builder()
                .name("btc")
                .price(BigDecimal.valueOf(65000))
                .build();
        coinRepository.save(coin);

        Order buyOrder = Order.builder()
                .orderSide(BUY)
                .orderType(LIMIT)
                .orderStatus(PENDING)
                .volume(40000)
                .limitPrice(BigDecimal.valueOf(65001))
                .member(member1)
                .coin(coin)
                .build();

        Order sellOrder = Order.builder()
                .orderSide(SELL)
                .orderType(LIMIT)
                .orderStatus(PENDING)
                .volume(40000)
                .limitPrice(BigDecimal.valueOf(64999))
                .member(member2)
                .coin(coin)
                .build();

        orderRepository.saveAll(List.of(buyOrder, sellOrder));

        //when
        Trade trade = orderMatcher.match(buyOrder, sellOrder);

        //then
        assertThat(trade).extracting("executedQuantity", "executedPrice", "executedVolume")
                .containsExactly(
                        BigDecimal.valueOf(40000).divide(BigDecimal.valueOf(64999), 8, RoundingMode.FLOOR),
                        BigDecimal.valueOf(64999),
                        40000
                );
        assertThat(buyOrder).extracting("orderSide", "volume", "orderStatus")
                .containsExactly(BUY, 0, FINISHED);

        assertThat(sellOrder).extracting("orderSide", "volume", "orderStatus")
                .containsExactly(SELL, 0, FINISHED);
    }

}
