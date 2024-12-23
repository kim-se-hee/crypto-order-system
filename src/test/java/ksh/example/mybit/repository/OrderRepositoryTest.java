package ksh.example.mybit.repository;

import ksh.example.mybit.domain.*;
import ksh.example.mybit.service.MatchingService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class OrderRepositoryTest {

    @Autowired OrderRepository orderRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired CoinRepository coinRepository;

    @Autowired MatchingService matchingService;

    @Test
    public void 미체결_매도_수량_조회() throws Exception{
        Member member1 = memberRepository.findById(1l).get();
        Member member2 = memberRepository.findById(2l).get();
        Coin btc = coinRepository.findById(1l).get();

        Order order1 = new Order(10000, OrderSide.SELL, OrderType.LIMIT, new BigDecimal(1000), member1, btc);
        Order order2 = new Order(20000, OrderSide.SELL, OrderType.LIMIT, new BigDecimal(1000), member1, btc);
        Order order3 = new Order(5000, OrderSide.BUY, OrderType.LIMIT, new BigDecimal(1000), member2, btc);

        orderRepository.save(order1);
        orderRepository.save(order2);
        orderRepository.save(order3);

        matchingService.matchOrder();

        Long sum = orderRepository.sumPendingOrderAmount(OrderSide.SELL, member1, btc);
        assertThat(sum).isEqualTo(25000);
    }

    @Test
    public void 미체결_매수_수량_조회() throws Exception{
        Member member1 = memberRepository.findById(1l).get();
        Member member2 = memberRepository.findById(2l).get();
        Coin btc = coinRepository.findById(1l).get();

        Order order1 = new Order(10000, OrderSide.BUY, OrderType.LIMIT, new BigDecimal(1000), member1, btc);
        Order order2 = new Order(20000, OrderSide.BUY, OrderType.LIMIT, new BigDecimal(1000), member1, btc);
        Order order3 = new Order(5000, OrderSide.SELL, OrderType.LIMIT, new BigDecimal(1000), member2, btc);

        orderRepository.save(order1);
        orderRepository.save(order2);
        orderRepository.save(order3);

        matchingService.matchOrder();

        Long sum = orderRepository.sumPendingOrderAmount(OrderSide.BUY, member1, null);
        assertThat(sum).isEqualTo(25000);
    }

}
