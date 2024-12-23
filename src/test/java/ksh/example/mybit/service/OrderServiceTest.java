package ksh.example.mybit.service;

import ksh.example.mybit.domain.*;
import ksh.example.mybit.repository.CoinRepository;
import ksh.example.mybit.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;


@SpringBootTest
class OrderServiceTest {
    @Autowired OrderService orderService;
    @Autowired MatchingService matchingService;

    @Autowired MemberRepository memberRepository;
    @Autowired CoinRepository coinRepository;

    @Test
    public void 주문_가능_원화_부족() throws Exception{
        Member member1 = memberRepository.findById(1l).get();
        Coin btc = coinRepository.findById(1l).get();
        Order order1 = new Order(30000, OrderSide.BUY, OrderType.LIMIT, new BigDecimal(1000), member1, btc);
        Order order2 = new Order(10001, OrderSide.BUY, OrderType.LIMIT, new BigDecimal(1000), member1, btc);
        orderService.placeOrder(order1);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {orderService.placeOrder(order2);});
    }

    @Test
    public void 주문_가능_수량_부족() throws Exception{
        Member member1 = memberRepository.findById(1l).get();
        Coin btc = coinRepository.findById(1l).get();
        Order order1 = new Order(5000, OrderSide.SELL, OrderType.LIMIT, new BigDecimal(1000), member1, btc);
        Order order2 = new Order(5001, OrderSide.SELL, OrderType.LIMIT, new BigDecimal(1000), member1, btc);
        orderService.placeOrder(order1);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {orderService.placeOrder(order2);});
    }
}
