package ksh.example.mybit.order.implementation;

import ksh.example.mybit.coin.domain.Coin;
import ksh.example.mybit.coin.repository.CoinRepository;
import ksh.example.mybit.member.domain.Member;
import ksh.example.mybit.member.repository.MemberRepository;
import ksh.example.mybit.membercoin.domain.MemberCoin;
import ksh.example.mybit.membercoin.repository.MemberCoinRepository;
import ksh.example.mybit.order.domain.Order;
import ksh.example.mybit.order.domain.OrderSide;
import ksh.example.mybit.order.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static ksh.example.mybit.order.domain.OrderSide.BUY;
import static ksh.example.mybit.order.domain.OrderSide.SELL;
import static ksh.example.mybit.order.domain.OrderStatus.PENDING;
import static ksh.example.mybit.order.domain.OrderType.MARKET;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class OrderValidatorTest {

    @Autowired
    private OrderValidator orderValidator;

    @Autowired
    private MemberCoinRepository memberCoinRepository;

    @Autowired
    private CoinRepository coinRepository;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private OrderRepository orderRepository;

    @DisplayName("매도 주문을 요청 시 주문량이 지갑 내 보유량 이하여야 한다")
    @Test
    void checkOrderVolumeIsValid1() {
        //given
        Coin coin = Coin.builder()
                .price(BigDecimal.valueOf(100))
                .build();
        coinRepository.save(coin);

        Member member = new Member();
        memberRepository.save(member);

        MemberCoin memberCoin = createMemberCoin(coin, member);
        memberCoinRepository.save(memberCoin);

        Order order = createOrder(SELL, 20000, member, coin);
        orderRepository.save(order);

        //when //then
        assertThatCode(
                () -> orderValidator.checkOrderVolumeIsValid(member.getId(), coin.getId(), 80000, SELL)
        ).doesNotThrowAnyException();

     }

    @DisplayName("매도 주문을 요청 시 주문량이 지갑 내 보유량을 초과하면 예외가 발생한다")
    @Test
    void checkOrderVolumeIsValid2() {
        //given
        Coin coin = Coin.builder()
                .price(BigDecimal.valueOf(100))
                .build();
        coinRepository.save(coin);

        Member member = new Member();
        memberRepository.save(member);

        MemberCoin memberCoin = createMemberCoin(coin, member);
        memberCoinRepository.save(memberCoin);

        Order order = createOrder(SELL, 20000, member, coin);
        orderRepository.save(order);

        //when //then
        assertThatThrownBy(() -> orderValidator.checkOrderVolumeIsValid(member.getId(), coin.getId(), 80001, SELL))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 가능한 수량이 부족합니다");

    }

    @DisplayName("매도 주문을 요청 시 매도 요청한 자산을 보유하고 있지 않으면 예외가 발생한다")
    @Test
    void checkOrderVolumeIsValid3() {

        //when //then
        assertThatThrownBy(() -> orderValidator.checkOrderVolumeIsValid(1l, 1l, 80001, SELL))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 자산을 보유하고 있지 않습니다");

    }

    @DisplayName("매수 주문을 요청 시 주문량이 지갑 내 원화량 이하여야 한다")
    @Test
    void checkAvailableKoreanWonBalance1(){
        //given
        Coin coin = Coin.builder()
                .ticker("won")
                .price(BigDecimal.valueOf(1))
                .build();
        coinRepository.save(coin);

        Member member = new Member();
        memberRepository.save(member);

        MemberCoin memberCoin = createWon(coin, member);
        memberCoinRepository.save(memberCoin);

        Order order = createOrder(BUY, 20000, member, coin);
        orderRepository.save(order);

        //when //then
        assertThatCode(
                () -> orderValidator.checkOrderVolumeIsValid(member.getId(), coin.getId(), 80000, BUY)
        ).doesNotThrowAnyException();
     }

    @DisplayName("매수 주문을 요청 시 주문량이 지갑 내 원화량보다 많으면 예외가 발생한다")
    @Test
    void checkAvailableKoreanWonBalance2(){
        //given
        Coin coin = Coin.builder()
                .ticker("won")
                .price(BigDecimal.valueOf(1))
                .build();
        coinRepository.save(coin);

        Member member = new Member();
        memberRepository.save(member);

        MemberCoin memberCoin = createWon(coin, member);
        memberCoinRepository.save(memberCoin);

        Order order = createOrder(BUY, 20000, member, coin);
        orderRepository.save(order);

        //when //then
        assertThatThrownBy(() -> orderValidator.checkOrderVolumeIsValid(member.getId(), coin.getId(), 80001, BUY))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 가능한 금액이 부족합니다");


    }

    private static Order createOrder(OrderSide orderSide, int volume, Member member, Coin coin) {
        return Order.builder()
                .orderSide(orderSide)
                .orderType(MARKET)
                .orderStatus(PENDING)
                .volume(volume)
                .member(member)
                .coin(coin)
                .build();
    }

    private static MemberCoin createMemberCoin(Coin coin, Member member) {
        return MemberCoin.builder()
                .coin(coin)
                .member(member)
                .quantity(BigDecimal.valueOf(1000))
                .build();
    }

    private static MemberCoin createWon(Coin coin, Member member) {
        return MemberCoin.builder()
                .coin(coin)
                .member(member)
                .quantity(BigDecimal.valueOf(100000))
                .build();
    }
}
