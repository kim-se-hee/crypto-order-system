package ksh.example.mybit.order.service;

import ksh.example.mybit.coin.domain.Coin;
import ksh.example.mybit.coin.repository.CoinRepository;
import ksh.example.mybit.member.domain.Member;
import ksh.example.mybit.member.repository.MemberRepository;
import ksh.example.mybit.membercoin.domain.MemberCoin;
import ksh.example.mybit.membercoin.repository.MemberCoinRepository;
import ksh.example.mybit.order.domain.Order;
import ksh.example.mybit.order.domain.OrderSide;
import ksh.example.mybit.order.domain.OrderStatus;
import ksh.example.mybit.order.repository.OrderRepository;
import ksh.example.mybit.order.service.dto.request.OpenOrderServiceRequest;
import ksh.example.mybit.order.service.dto.request.OrderCreateServiceRequest;
import ksh.example.mybit.order.service.dto.response.OrderCreateResponse;
import ksh.example.mybit.order.service.dto.response.OrderListResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static ksh.example.mybit.order.domain.OrderSide.BUY;
import static ksh.example.mybit.order.domain.OrderSide.SELL;
import static ksh.example.mybit.order.domain.OrderStatus.CANCELED;
import static ksh.example.mybit.order.domain.OrderStatus.PENDING;
import static ksh.example.mybit.order.domain.OrderType.LIMIT;
import static ksh.example.mybit.order.domain.OrderType.MARKET;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CoinRepository coinRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MemberCoinRepository memberCoinRepository;

    @DisplayName("매수 주문을 생성한다")
    @Test
    void placeOrder1(){
        //given
        Coin coin1 = createCoin("btc", 65000);
        Coin coin2 = createCoin("eth", 2000);
        Coin won = createCoin("won", 1);
        coinRepository.saveAll(List.of(coin1, coin2, won));

        Member member = new Member();
        memberRepository.save(member);

        MemberCoin memberWon = createMemberCoin(won, member, 20000);
        memberCoinRepository.save(memberWon);

        Order order = createLimitOrder(BUY, 10000, BigDecimal.valueOf(1000), member, coin1);
        orderRepository.save(order);

        OrderCreateServiceRequest request = OrderCreateServiceRequest.builder()
                .memberId(member.getId())
                .coinId(coin2.getId())
                .orderType(MARKET)
                .orderSide(BUY)
                .orderVolume(10000)
                .build();
        //when
        OrderCreateResponse response = orderService.placeOrder(request);

        //then
        Order createdOrder = orderRepository.findById(response.getId()).get();
        assertThat(createdOrder)
                .extracting("orderSide", "orderType", "member.id", "coin.id", "volume")
                .containsExactly(BUY, MARKET, member.getId(), coin2.getId(), 10000);
    }

    @DisplayName("주문 가능 금액보다 매수 요청액이 더 크면 주문을 요청 시 예외가 발생한다")
    @Test
    void placeOrder2(){
        //given
        Coin coin1 = createCoin("btc", 65000);
        Coin coin2 = createCoin("eth", 2000);
        Coin won = createCoin("won", 1);
        coinRepository.saveAll(List.of(coin1, coin2, won));

        Member member = new Member();
        memberRepository.save(member);

        MemberCoin memberWon = createMemberCoin(won, member, 20000);
        memberCoinRepository.save(memberWon);

        Order order = createLimitOrder(BUY, 10000, BigDecimal.valueOf(1000), member, coin1);
        orderRepository.save(order);

        OrderCreateServiceRequest request = OrderCreateServiceRequest.builder()
                .memberId(member.getId())
                .coinId(coin2.getId())
                .orderType(MARKET)
                .orderSide(BUY)
                .orderVolume(10001)
                .build();
        //when //then
        assertThatThrownBy(() -> orderService.placeOrder(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 가능한 금액이 부족합니다");
    }

    @DisplayName("매도 주문을 생성한다")
    @Test
    void placeOrder3(){
        //given
        Coin coin = createCoin("btc", 1);
        coinRepository.save(coin);

        Member member = new Member();
        memberRepository.save(member);

        MemberCoin memberWon = createMemberCoin(coin, member, 20000);
        memberCoinRepository.save(memberWon);

        Order order = Order.builder()
                .orderSide(SELL)
                .orderType(MARKET)
                .orderStatus(PENDING)
                .member(member)
                .limitPrice(BigDecimal.valueOf(1000))
                .volume(10000)
                .coin(coin)
                .createdAt(LocalDateTime.of(2025, 2, 13, 0, 0))
                .build();

        orderRepository.save(order);

        OrderCreateServiceRequest request = OrderCreateServiceRequest.builder()
                .memberId(member.getId())
                .coinId(coin.getId())
                .orderType(MARKET)
                .orderSide(SELL)
                .orderVolume(10000)
                .build();
        //when
        OrderCreateResponse response = orderService.placeOrder(request);

        //then
        Order createdOrder = orderRepository.findById(response.getId()).get();
        assertThat(createdOrder)
                .extracting("orderSide", "orderType", "member.id", "coin.id", "volume")
                .containsExactly(SELL, MARKET, member.getId(), coin.getId(), 10000);
    }

    @DisplayName("주문 가능 금액보다 매도 요청액이 더 크면 주문을 요청 시 예외가 발생한다")
    @Test
    void placeOrder4(){
        //given
        Coin coin = createCoin("btc", 1);
        coinRepository.save(coin);

        Member member = new Member();
        memberRepository.save(member);

        MemberCoin memberWon = createMemberCoin(coin, member, 20000);
        memberCoinRepository.save(memberWon);

        Order order = Order.builder()
                .orderSide(SELL)
                .orderType(MARKET)
                .orderStatus(PENDING)
                .member(member)
                .limitPrice(BigDecimal.valueOf(1000))
                .volume(10000)
                .coin(coin)
                .createdAt(LocalDateTime.of(2025, 2, 13, 0, 0))
                .build();

        orderRepository.save(order);

        OrderCreateServiceRequest request = OrderCreateServiceRequest.builder()
                .memberId(member.getId())
                .coinId(coin.getId())
                .orderType(MARKET)
                .orderSide(SELL)
                .orderVolume(10001)
                .build();

        //when //then
        assertThatThrownBy(() -> orderService.placeOrder(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 가능한 수량이 부족합니다");
    }

    @DisplayName("요청한 주문을 취소한다")
    @Test
    void cancelOrder1(){
        //given
        Coin coin = Coin.builder()
                .ticker("btc")
                .price(BigDecimal.TEN)
                .build();
        coinRepository.save(coin);

        Member member1 = new Member();
        Member member2 = new Member();
        memberRepository.saveAll(List.of(member1, member2));

        Order order = createLimitOrder(BUY, 10000, BigDecimal.valueOf(1000), member1, coin);
        orderRepository.save(order);

        //when
        orderService.cancelOrder(order.getId());

        //then
        assertThat(order.getOrderStatus()).isEqualTo(CANCELED);

    }

    @DisplayName("요청한 적이 없는 주문이라면 예외가 발생한다")
    @Test
    void cancelOrder2(){
        //when //then
        assertThatThrownBy(() -> orderService.cancelOrder(1l))
                .isInstanceOf(NoSuchElementException.class);

    }

    @DisplayName("회원은 특정 코인의 미체결 주문 정보를 조회할 수 있다")
    @Test
    void getOpenOrders1() {
        //given
        Coin coin = Coin.builder()
                .ticker("btc")
                .price(BigDecimal.TEN)
                .build();
        coinRepository.save(coin);

        Member member1 = new Member();
        Member member2 = new Member();
        memberRepository.saveAll(List.of(member1, member2));

        Order limitOrder1 = createLimitOrder(BUY, 10000, BigDecimal.valueOf(1000), member1, coin);
        Order limitOrder2 = createLimitOrder(SELL, 5000 , BigDecimal.valueOf(1000), member1, coin);
        Order marketOrder = createMarketOrder(BUY, 6000, member1, coin);
        orderRepository.saveAll(List.of(limitOrder1, limitOrder2, marketOrder));

        PageRequest pageRequest = PageRequest.of(0, 3);
        OpenOrderServiceRequest request = OpenOrderServiceRequest.builder()
                .memberId(member1.getId())
                .coinId(coin.getId())
                .build();

        //when
        OrderListResponse response = orderService.getOpenOrders(request, pageRequest);

        //then
        assertThat(response.getOrderResponses()).hasSize(3)
                .extracting("ticker", "orderSide", "volume", "quantity")
                .containsExactlyInAnyOrder(
                        tuple("btc", BUY, 10000, new BigDecimal("1000.00000000")),
                        tuple("btc", SELL, 5000, new BigDecimal("500.00000000")),
                        tuple("btc", BUY, 6000, new BigDecimal("600.00000000"))
                );
     }

    @DisplayName("보유하지 않은 코인의 미체결 주문 정보를 조회하면 빈 리스트가 조회된다.")
    @Test
    void getOpenOrders2() {
        //given
        PageRequest pageRequest = PageRequest.of(0, 3);
        OpenOrderServiceRequest request = OpenOrderServiceRequest.builder()
                .memberId(1l)
                .coinId(3l)
                .build();

        //when
        OrderListResponse response = orderService.getOpenOrders(request, pageRequest);

        //then
        assertThat(response.getOrderResponses()).isEmpty();
    }

    private static Order createMarketOrder(OrderSide orderSide, int volume, Member member, Coin coin) {
        return Order.builder()
                .orderSide(orderSide)
                .orderType(MARKET)
                .orderStatus(PENDING)
                .member(member)
                .volume(volume)
                .coin(coin)
                .build();
    }

    private static Order createLimitOrder(OrderSide orderSide, int volume, BigDecimal limitPrice, Member member, Coin coin) {
        return Order.builder()
                .orderSide(orderSide)
                .orderType(LIMIT)
                .orderStatus(OrderStatus.PENDING)
                .limitPrice(limitPrice)
                .member(member)
                .volume(volume)
                .coin(coin)
                .build();
    }

    private static MemberCoin createMemberCoin(Coin coin, Member member, int quantity) {
        return MemberCoin.builder()
                .coin(coin)
                .member(member)
                .quantity(BigDecimal.valueOf(quantity))
                .build();
    }

    private static Coin createCoin(String ticker, int price) {
        return Coin.builder()
                .ticker(ticker)
                .price(BigDecimal.valueOf(price))
                .build();
    }
}
