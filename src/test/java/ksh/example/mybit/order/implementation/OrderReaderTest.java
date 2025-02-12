package ksh.example.mybit.order.implementation;

import ksh.example.mybit.coin.domain.Coin;
import ksh.example.mybit.coin.repository.CoinRepository;
import ksh.example.mybit.member.domain.Member;
import ksh.example.mybit.member.repository.MemberRepository;
import ksh.example.mybit.order.domain.Order;
import ksh.example.mybit.order.domain.OrderSide;
import ksh.example.mybit.order.domain.OrderStatus;
import ksh.example.mybit.order.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

import static ksh.example.mybit.order.domain.OrderSide.BUY;
import static ksh.example.mybit.order.domain.OrderSide.SELL;
import static ksh.example.mybit.order.domain.OrderStatus.*;
import static ksh.example.mybit.order.domain.OrderType.LIMIT;
import static ksh.example.mybit.order.domain.OrderType.MARKET;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class OrderReaderTest {

    @Autowired
    private OrderReader orderReader;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CoinRepository coinRepository;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("시장가 주문과 지정가 주문이 함께 있다면 최우선 시장가 주문이 조회된다")
    @Test
    void readMostPriorOrder1(){
        //given
        Coin coin = new Coin();
        coinRepository.save(coin);

        Order marketOrder1 = Order.builder()
                .orderType(MARKET)
                .orderStatus(PENDING)
                .coin(coin)
                .build();

        Order marketOrder2 = Order.builder()
                .orderType(MARKET)
                .orderStatus(PENDING)
                .coin(coin)
                .build();

        Order limitOrder = Order.builder()
                .orderType(LIMIT)
                .orderStatus(PENDING)
                .coin(coin)
                .build();
        orderRepository.saveAll(List.of(marketOrder1, marketOrder2, limitOrder));

        //when
        Order mostPriorOrder = orderReader.readMostPriorOrder(coin.getId());

        //then
        assertThat(mostPriorOrder).isEqualTo(marketOrder1);
     }

    @DisplayName("지정가 주문만 있다면 최우선 지정가 매수 주문이 조회된다")
    @Test
    void readMostPriorOrder2(){
        //given
        Coin coin = new Coin();
        coinRepository.save(coin);

        Order limitOrder1 = createLimitOrder(BUY, PENDING, BigDecimal.valueOf(1000), null, coin);
        Order limitOrder2 = createLimitOrder(BUY, PENDING, BigDecimal.valueOf(100), null, coin);
        Order limitOrder3 = createLimitOrder(SELL, PENDING, BigDecimal.valueOf(100), null, coin);

        orderRepository.saveAll(List.of(limitOrder1, limitOrder2, limitOrder3));

        //when
        Order mostPriorOrder = orderReader.readMostPriorOrder(coin.getId());

        //then
        assertThat(mostPriorOrder).isEqualTo(limitOrder1);
    }

    @DisplayName("시장가 주문이나 지정가 매수 주문이 없다면 예외가 발생한다")
    @Test
    void readMostPriorOrder3(){
        //given
        Coin coin = new Coin();
        coinRepository.save(coin);

        Order limitOrder1 = createLimitOrder(SELL, PENDING, BigDecimal.valueOf(100), null, coin);
        orderRepository.save(limitOrder1);

        //when //then
        assertThatThrownBy(() -> orderReader.readMostPriorOrder(coin.getId()))
                .isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("생성된 주문이 없다면 예외가 발생한다")
    @Test
    void readMostPriorOrder4(){
        //when //then
        assertThatThrownBy(() -> orderReader.readMostPriorOrder(1l))
                .isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("주어진 주문과 매칭이 되는 최우선 주문을 찾는다")
    @Test
    void readMatchingOrder1(){
        //given
        Coin coin = new Coin();
        coinRepository.save(coin);

        Member member1 = new Member();
        Member member2 = new Member();
        memberRepository.saveAll(List.of(member1, member2));

        Order limitOrder1 = createLimitOrder(BUY, PENDING, BigDecimal.valueOf(1000), member1, coin);
        Order limitOrder2 = createLimitOrder(SELL, PENDING, BigDecimal.valueOf(100), member2, coin);
        orderRepository.saveAll(List.of(limitOrder1, limitOrder2));

        //when
        Order matchingOrder = orderReader.readMatchingOrder(limitOrder1);

        //then
        assertThat(matchingOrder).isEqualTo(limitOrder2);
     }

    @DisplayName("주어진 주문과 매칭이 되는 주문이 없으면 예외가 발생한다")
    @Test
    void readMatchingOrder2(){
        //given
        Coin coin = new Coin();
        coinRepository.save(coin);

        Member member1 = new Member();
        Member member2 = new Member();
        memberRepository.saveAll(List.of(member1, member2));

        Order limitOrder1 = createLimitOrder(BUY, PENDING, BigDecimal.valueOf(1000), member1, coin);
        Order limitOrder2 = createLimitOrder(SELL, PENDING, BigDecimal.valueOf(10000), member2, coin);
        orderRepository.saveAll(List.of(limitOrder1, limitOrder2));

        //when //then
        assertThatThrownBy(() -> orderReader.readMatchingOrder(limitOrder1))
                .isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("회원은 특정 코인의 미체결 주문 목록을 조회할 수 있다")
    @Test
    void readPendingOrdersBy1(){
        //given
        Coin coin = new Coin();
        coinRepository.save(coin);

        Member member1 = new Member();
        Member member2 = new Member();
        memberRepository.saveAll(List.of(member1, member2));

        Order limitOrder1 = createLimitOrder(BUY, PENDING, BigDecimal.valueOf(1000), member1, coin);
        Order limitOrder2 = createLimitOrder(BUY, PENDING, BigDecimal.valueOf(1000), member1, coin);
        Order marketOrder = createMarketOrder(BUY, PENDING, member1, coin);
        orderRepository.saveAll(List.of(limitOrder1, limitOrder2, marketOrder));

        PageRequest pageRequest = PageRequest.of(0, 2);

        //when
        List<Order> orders = orderReader.readPendingOrdersBy(member1.getId(), coin.getId(), pageRequest);

        //then
        assertThat(orders).hasSize(2)
                .containsExactlyInAnyOrder(limitOrder1, limitOrder2);
     }

    @DisplayName("특정 코인에 대한 미체결 주문이 없다면 빈 리스트가 조회된다")
    @Test
    void readPendingOrdersBy2(){
        //given
        Coin coin = new Coin();
        coinRepository.save(coin);

        Member member = new Member();
        Member member2 = new Member();
        memberRepository.saveAll(List.of(member, member2));

        Order limitOrder1 = createLimitOrder(BUY, FINISHED, BigDecimal.valueOf(1000), member, coin);
        Order limitOrder2 = createLimitOrder(BUY, CANCELED, BigDecimal.valueOf(1000), member, coin);
        Order marketOrder = createMarketOrder(BUY, FINISHED, member, coin);
        orderRepository.saveAll(List.of(limitOrder1, limitOrder2, marketOrder));

        PageRequest pageRequest = PageRequest.of(0, 3);

        //when
        List<Order> orders = orderReader.readPendingOrdersBy(member.getId(), coin.getId(), pageRequest);

        //then
        assertThat(orders).isEmpty();
    }

    private static Order createMarketOrder(OrderSide orderSide, OrderStatus status, Member member, Coin coin) {
        return Order.builder()
                .orderSide(orderSide)
                .orderType(MARKET)
                .orderStatus(status)
                .member(member)
                .coin(coin)
                .build();
    }

    private static Order createLimitOrder(OrderSide orderSide, OrderStatus status, BigDecimal limitPrice, Member member, Coin coin) {
        return Order.builder()
                .orderSide(orderSide)
                .orderType(LIMIT)
                .orderStatus(status)
                .limitPrice(limitPrice)
                .member(member)
                .coin(coin)
                .build();
    }
}
