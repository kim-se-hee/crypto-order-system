package ksh.example.mybit.order.repository;

import jakarta.persistence.EntityManager;
import ksh.example.mybit.coin.domain.Coin;
import ksh.example.mybit.coin.repository.CoinRepository;
import ksh.example.mybit.member.domain.Member;
import ksh.example.mybit.member.repository.MemberRepository;
import ksh.example.mybit.order.domain.Order;
import ksh.example.mybit.order.domain.OrderType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static ksh.example.mybit.order.domain.OrderSide.BUY;
import static ksh.example.mybit.order.domain.OrderSide.SELL;
import static ksh.example.mybit.order.domain.OrderStatus.*;
import static ksh.example.mybit.order.domain.OrderType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@SpringBootTest
@Transactional
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CoinRepository coinRepository;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("최우선 시장가 주문을 조회하면 먼저 생성된 미완료 시장가 주문이 조회된다")
    @Test
    void findMostPriorMarketOrder1(){
        //given
        Coin coin = Coin.builder().build();
        coinRepository.save(coin);

        Order ealryOrder = Order.builder()
                .orderType(MARKET)
                .orderStatus(PENDING)
                .coin(coin)
                .build();
        Order lateOrder = Order.builder()
                .orderType(MARKET)
                .orderStatus(PENDING)
                .coin(coin)
                .build();

        orderRepository.save(ealryOrder);
        orderRepository.save(lateOrder);

        //when
        Order findOrder = orderRepository.findMostPriorMarketOrderBy(coin.getId()).get();

        //then
        assertThat(findOrder).isEqualTo(ealryOrder);

     }

    @DisplayName("시장가 주문이 없다면 최우선 시장가 주문 조회 시 조회되는 주문이 없다.")
    @Test
    void findMostPriorMarketOrder2(){

        //when
        Optional<Order> findOrder = orderRepository.findMostPriorMarketOrderBy(1l);

        //then
        assertThat(findOrder).isEmpty();

    }

    @DisplayName("생성 시간이 같을 때 최우선 시장가 주문을 조회하면 주문량이 더 큰 미완료 시장가 주문이 조회된다")
    @Test
    void findMostPriorMarketOrder3(){
        //given
        Coin coin = Coin.builder().build();
        coinRepository.save(coin);

        LocalDateTime createdDateTime = LocalDateTime.of(2025, 1, 1, 1, 1, 1, 1);

        Order largeOrder = Order.builder()
                .orderType(MARKET)
                .orderStatus(PENDING)
                .coin(coin)
                .createdAt(createdDateTime)
                .volume(10000)
                .build();

        Order smallOrder = Order.builder()
                .orderType(MARKET)
                .orderStatus(PENDING)
                .coin(coin)
                .createdAt(createdDateTime)
                .volume(9000)
                .build();

        orderRepository.save(largeOrder);
        orderRepository.save(smallOrder);

        //when
        Order findOrder = orderRepository.findMostPriorMarketOrderBy(coin.getId()).get();

        //then
        assertThat(findOrder).isEqualTo(largeOrder);

    }

    @DisplayName("최우선 지정가 주문을 조회하면 가격이 가장 비싼 지정가의 미완료 매수 주문이 조회된다.")
    @Test
    void findMostPriorLimitOrder1(){
        //given
        Coin coin = Coin.builder().build();
        coinRepository.save(coin);

        Order expensvieOrder = Order.builder()
                .orderSide(BUY)
                .orderType(LIMIT)
                .orderStatus(PENDING)
                .limitPrice(BigDecimal.valueOf(1000))
                .coin(coin)
                .build();

        Order cheapOrder = Order.builder()
                .orderSide(BUY)
                .orderType(MARKET)
                .orderStatus(PENDING)
                .limitPrice(BigDecimal.valueOf(100))
                .coin(coin)
                .build();

        orderRepository.save(cheapOrder);
        orderRepository.save(expensvieOrder);

        //when
        Order findOrder = orderRepository.findMostPriorLimitOrderBy(coin.getId()).get();

        //then
        assertThat(findOrder).isEqualTo(expensvieOrder);

     }

    @DisplayName("지정가가 같은 주문이 있다면 최우선 지정가 주문을 조회했을 때 가장 먼저 생성된 미완료 매수 주문이 조회된다.")
    @Test
    void findMostPriorLimitOrder2(){
        //given
        Coin coin = Coin.builder().build();
        coinRepository.save(coin);

        Order earlyOrder = Order.builder()
                .orderSide(BUY)
                .orderType(LIMIT)
                .orderStatus(PENDING)
                .limitPrice(BigDecimal.valueOf(1000))
                .coin(coin)
                .build();

        Order lateOrder = Order.builder()
                .orderSide(BUY)
                .orderType(MARKET)
                .orderStatus(PENDING)
                .limitPrice(BigDecimal.valueOf(1000))
                .coin(coin)
                .build();

        orderRepository.save(earlyOrder);
        orderRepository.save(lateOrder);

        //when
        Order findOrder = orderRepository.findMostPriorLimitOrderBy(coin.getId()).get();

        //then
        assertThat(findOrder).isEqualTo(earlyOrder);

    }

    @DisplayName("지정가와 생성 시간이 같은 주문이 있다면 최우선 지정가 주문을 조회했을 때 주문량이 가장 큰 미완료 매수 주문이 조회된다.")
    @Test
    void findMostPriorLimitOrder3(){
        //given
        Coin coin = Coin.builder().build();
        coinRepository.save(coin);

        LocalDateTime createdDateTime = LocalDateTime.of(2025, 1, 1, 1, 1, 1, 1);

        Order largeOrder = Order.builder()
                .orderSide(BUY)
                .orderType(LIMIT)
                .orderStatus(PENDING)
                .limitPrice(BigDecimal.valueOf(1000))
                .coin(coin)
                .volume(1000)
                .createdAt(createdDateTime)
                .build();

        Order smallOrder = Order.builder()
                .orderSide(BUY)
                .orderType(MARKET)
                .orderStatus(PENDING)
                .limitPrice(BigDecimal.valueOf(1000))
                .coin(coin)
                .volume(900)
                .createdAt(createdDateTime)
                .build();

        orderRepository.save(largeOrder);
        orderRepository.save(smallOrder);

        //when
        Order findOrder = orderRepository.findMostPriorLimitOrderBy(coin.getId()).get();

        //then
        assertThat(findOrder).isEqualTo(largeOrder);

    }

    @DisplayName("지정가 매수 주문이 없다면 최우선 지정가 주문 조회 시 조회되는 것이 없다")
    @Test
    void findMostPriorLimitOrder4(){

        //when
        Optional<Order> findOrder = orderRepository.findMostPriorLimitOrderBy(1l);

        //then
        assertThat(findOrder).isEmpty();

    }

    @DisplayName("시장가 매도 주문은 가장 우선 순위가 높은 매수 주문과 매칭이 된다.")
    @Test
    void findMatchingOrder1(){
        //given
        Member member1 = new Member();
        Member member2 = new Member();
        Coin coin = new Coin();

        memberRepository.save(member1);
        memberRepository.save(member2);
        coinRepository.save(coin);

        Order marketSellOrder = Order.builder()
                .orderSide(SELL)
                .orderType(MARKET)
                .orderStatus(PENDING)
                .member(member1)
                .coin(coin)
                .build();

        Order buyOrder1 = Order.builder()
                .orderSide(BUY)
                .orderType(LIMIT)
                .orderStatus(PENDING)
                .limitPrice(BigDecimal.valueOf(1000))
                .member(member2)
                .coin(coin)
                .build();

        Order buyOrder2 = Order.builder()
                .orderSide(BUY)
                .orderType(LIMIT)
                .orderStatus(PENDING)
                .limitPrice(BigDecimal.valueOf(9000))
                .member(member2)
                .coin(coin)
                .build();

        orderRepository.saveAll(List.of(marketSellOrder, buyOrder1, buyOrder2));

        //when
        Order matchingOrder = orderRepository.findMatchingOrder(marketSellOrder).get();

        //then
        assertThat(matchingOrder).isEqualTo(buyOrder2);
     }

    @DisplayName("시장가 매수 주문은 가장 우선 순위가 높은 매도 주문과 매칭이 된다.")
    @Test
    void findMatchingOrder2(){
        //given
        Member member1 = new Member();
        Member member2 = new Member();
        Coin coin = new Coin();

        memberRepository.save(member1);
        memberRepository.save(member2);
        coinRepository.save(coin);

        Order marketBuyOrder = Order.builder()
                .orderSide(BUY)
                .orderType(MARKET)
                .orderStatus(PENDING)
                .member(member1)
                .coin(coin)
                .build();

        Order buyOrder1 = Order.builder()
                .orderSide(SELL)
                .orderType(LIMIT)
                .orderStatus(PENDING)
                .limitPrice(BigDecimal.valueOf(1000))
                .member(member2)
                .coin(coin)
                .build();

        Order buyOrder2 = Order.builder()
                .orderSide(SELL)
                .orderType(LIMIT)
                .orderStatus(PENDING)
                .limitPrice(BigDecimal.valueOf(9000))
                .member(member2)
                .coin(coin)
                .build();

        orderRepository.saveAll(List.of(marketBuyOrder, buyOrder1, buyOrder2));

        //when
        Order matchingOrder = orderRepository.findMatchingOrder(marketBuyOrder).get();

        //then
        assertThat(matchingOrder).isEqualTo(buyOrder1);
    }

    @DisplayName("지정가 매수 주문은 지정가가 자신 이하인 매도 주문 중 가장 우선 순위가 높은 매도 주문과 매칭이 된다.")
    @Test
    void findMatchingOrder3(){
        //given
        Member member1 = new Member();
        Member member2 = new Member();
        Coin coin = new Coin();

        memberRepository.save(member1);
        memberRepository.save(member2);
        coinRepository.save(coin);

        Order limitBuyOrder = Order.builder()
                .orderSide(BUY)
                .orderType(LIMIT)
                .orderStatus(PENDING)
                .limitPrice(BigDecimal.valueOf(1000))
                .member(member1)
                .coin(coin)
                .build();

        Order buyOrder1 = Order.builder()
                .orderSide(SELL)
                .orderType(LIMIT)
                .orderStatus(PENDING)
                .limitPrice(BigDecimal.valueOf(1001))
                .member(member2)
                .coin(coin)
                .build();

        Order buyOrder2 = Order.builder()
                .orderSide(SELL)
                .orderType(LIMIT)
                .orderStatus(PENDING)
                .limitPrice(BigDecimal.valueOf(1000))
                .member(member2)
                .coin(coin)
                .build();

        Order buyOrder3 = Order.builder()
                .orderSide(SELL)
                .orderType(LIMIT)
                .orderStatus(PENDING)
                .limitPrice(BigDecimal.valueOf(999))
                .member(member2)
                .coin(coin)
                .build();

        orderRepository.saveAll(List.of(limitBuyOrder, buyOrder1, buyOrder2, buyOrder3));

        //when
        Order matchingOrder = orderRepository.findMatchingOrder(limitBuyOrder).get();

        //then
        assertThat(matchingOrder).isEqualTo(buyOrder3);
    }

    @DisplayName("지정가 매수 주문은 지정가가 자신 이하인 매도 주문이 없다면 매칭되지 않는다.")
    @Test
    void findMatchingOrder4(){
        //given
        Member member1 = new Member();
        Member member2 = new Member();
        Coin coin = new Coin();

        memberRepository.save(member1);
        memberRepository.save(member2);
        coinRepository.save(coin);

        Order limitBuyOrder = Order.builder()
                .orderSide(BUY)
                .orderType(LIMIT)
                .orderStatus(PENDING)
                .limitPrice(BigDecimal.valueOf(1000))
                .member(member1)
                .coin(coin)
                .build();

        Order buyOrder1 = Order.builder()
                .orderSide(SELL)
                .orderType(LIMIT)
                .orderStatus(PENDING)
                .limitPrice(BigDecimal.valueOf(1001))
                .member(member2)
                .coin(coin)
                .build();

        Order buyOrder2 = Order.builder()
                .orderSide(SELL)
                .orderType(LIMIT)
                .orderStatus(PENDING)
                .limitPrice(BigDecimal.valueOf(1001))
                .member(member2)
                .coin(coin)
                .build();

        orderRepository.saveAll(List.of(limitBuyOrder, buyOrder1, buyOrder2));

        //when
        Optional<Order> matchingOrder = orderRepository.findMatchingOrder(limitBuyOrder);

        //then
        assertThat(matchingOrder).isEmpty();
    }

    @DisplayName("특정 코인에 대한 자신의 미체결 주문 합계를 알 수 있다")
    @Test
    void sumPendingOrderVolume1(){
        //given
        Member member = new Member();
        Coin coin = new Coin();

        memberRepository.save(member);
        coinRepository.save(coin);

        Order order1 = Order.builder()
                .orderSide(BUY)
                .orderType(LIMIT)
                .member(member)
                .coin(coin)
                .orderStatus(PENDING)
                .volume(200)
                .build();

        Order order2 = Order.builder()
                .orderSide(BUY)
                .orderType(MARKET)
                .member(member)
                .coin(coin)
                .orderStatus(PENDING)
                .volume(100)
                .build();

        Order order3 = Order.builder()
                .orderSide(BUY)
                .orderType(LIMIT)
                .member(member)
                .coin(coin)
                .orderStatus(FINISHED)
                .volume(400)
                .build();

        orderRepository.saveAll(List.of(order1, order2, order3));

        //when
        Long sum = orderRepository.sumPendingOrderVolume(BUY, member.getId(), coin.getId());

        //then
        assertThat(sum).isEqualTo(300);
     }

    @DisplayName("모든 주문이 처리된 경우 미체결 주문량 총합이 0이다")
    @Test
    void sumPendingOrderVolume2(){
        //given
        Member member = new Member();
        Coin coin = new Coin();

        memberRepository.save(member);
        coinRepository.save(coin);

        Order order1 = Order.builder()
                .orderSide(BUY)
                .orderType(LIMIT)
                .member(member)
                .coin(coin)
                .orderStatus(FINISHED)
                .volume(200)
                .build();

        Order order2 = Order.builder()
                .orderSide(BUY)
                .orderType(MARKET)
                .member(member)
                .coin(coin)
                .orderStatus(CANCELED)
                .volume(100)
                .build();

        Order order3 = Order.builder()
                .orderSide(BUY)
                .orderType(LIMIT)
                .member(member)
                .coin(coin)
                .orderStatus(FINISHED)
                .volume(400)
                .build();

        orderRepository.saveAll(List.of(order1, order2, order3));

        //when
        Long sum = orderRepository.sumPendingOrderVolume(BUY, member.getId(), coin.getId());

        //then
        assertThat(sum).isZero();
    }

    @DisplayName("회원은 특정 코인의 시간 순으로 정렬된 미체결 주문 목록을 페이징하여 조회할 수 있다")
    @Test
    void findPendingOrderBy1(){
        //given
        Member member = new Member();
        Coin coin = new Coin();

        memberRepository.save(member);
        coinRepository.save(coin);

        Order order1 = Order.builder()
                .orderSide(BUY)
                .member(member)
                .coin(coin)
                .orderStatus(PENDING)
                .volume(200)
                .createdAt(LocalDateTime.of(2020, 2, 2, 12, 0))
                .build();

        Order order2 = Order.builder()
                .orderSide(BUY)
                .member(member)
                .coin(coin)
                .orderStatus(PENDING)
                .volume(100)
                .createdAt(LocalDateTime.of(2020, 2, 2, 12, 1))
                .build();

        Order order3 = Order.builder()
                .orderSide(BUY)
                .member(member)
                .coin(coin)
                .orderStatus(PENDING)
                .volume(400)
                .createdAt(LocalDateTime.of(2020, 2, 2, 12, 2))
                .build();

        orderRepository.saveAll(List.of(order1, order2, order3));

        PageRequest pageRequest = PageRequest.of(0, 2);

        //when
        List<Order> pendingOrders = orderRepository.findPendingOrdersBy(member.getId(), coin.getId(), pageRequest);

        //then
        assertThat(pendingOrders).hasSize(2)
                .extracting("orderStatus", "volume")
                .containsExactlyInAnyOrder(
                        tuple(PENDING, 200),
                        tuple(PENDING, 100)
                );

        assertThat(pendingOrders).isSortedAccordingTo(
                Comparator.comparing(Order::getCreatedAt));
     }

     @DisplayName("회원은 특정 코인의 미체결 주문 목록을 조회 시 모든 주문이 처리됐다면 조회되는 주문이 없다")
     @Test
     void findPendingOrderBy2(){
         //given
         Member member = new Member();
         Coin coin = new Coin();

         memberRepository.save(member);
         coinRepository.save(coin);

         Order order1 = Order.builder()
                 .orderSide(BUY)
                 .member(member)
                 .coin(coin)
                 .orderStatus(CANCELED)
                 .build();

         Order order2 = Order.builder()
                 .orderSide(BUY)
                 .member(member)
                 .coin(coin)
                 .orderStatus(FINISHED)
                 .build();

         Order order3 = Order.builder()
                 .orderSide(BUY)
                 .member(member)
                 .coin(coin)
                 .orderStatus(CANCELED)
                 .build();

         orderRepository.saveAll(List.of(order1, order2, order3));
         //when
         List<Order> pendingOrders = orderRepository.findPendingOrdersBy(member.getId(), coin.getId(), PageRequest.of(0, 10));

         //then
         assertThat(pendingOrders).isEmpty();
      }

      @DisplayName("목표가에 도달하면 자동 주문이 지정가 주문이 생성된다")
      @ParameterizedTest
      @CsvSource({"100,LIMIT", "90,LIMIT"})
      void updateTriggeredOrders1(int stopPrice, OrderType orderType){
          //given
          Coin coin = Coin.builder()
                  .price(BigDecimal.valueOf(100))
                  .previousPrice(BigDecimal.valueOf(90))
                  .build();

         coinRepository.save(coin);

          Order order = Order.builder()
                  .orderType(PRE)
                  .stopPrice(BigDecimal.valueOf(stopPrice))
                  .coin(coin)
                  .build();

          orderRepository.save(order);

          //when
          orderRepository.updateOrderStatusOfTriggeredOrders();
          Order findOrder = orderRepository.findById(order.getId()).get();

          //then
          assertThat(findOrder).extracting("orderType").isEqualTo(orderType);
       }

    @DisplayName("목표가에 도달하지 않으면 지정가 주문이 생성되지 않는다")
    @ParameterizedTest
    @CsvSource({"101,PRE", "89,PRE"})
    void updateTriggeredOrders2(int stopPrice, OrderType orderType){
        //given
        Coin coin = Coin.builder()
                .price(BigDecimal.valueOf(100))
                .previousPrice(BigDecimal.valueOf(90))
                .build();

        coinRepository.save(coin);

        Order order = Order.builder()
                .orderType(PRE)
                .stopPrice(BigDecimal.valueOf(stopPrice))
                .coin(coin)
                .build();

        orderRepository.save(order);

        //when
        orderRepository.updateOrderStatusOfTriggeredOrders();
        Order findOrder = orderRepository.findById(order.getId()).get();

        //then
        assertThat(findOrder).extracting("orderType").isEqualTo(orderType);
    }

}
