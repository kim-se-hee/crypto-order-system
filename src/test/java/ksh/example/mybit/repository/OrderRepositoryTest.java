package ksh.example.mybit.repository;

import ksh.example.mybit.domain.Order;
import ksh.example.mybit.domain.OrderSide;
import ksh.example.mybit.domain.OrderType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class OrderRepositoryTest {
    @Autowired
    OrderRepository repository;

    @Test
    public void 지정가_매수_주문과_매칭되는_매도_주문_조회1() throws Exception{
        List<Order> matchingSellOrders = repository.findMatchingOrders(1l, OrderSide.BUY, OrderType.LIMIT, new BigDecimal(60000));
        assertThat(matchingSellOrders.size()).isEqualTo(0);
        for (Order sellOrder : matchingSellOrders) {
            assertThat(sellOrder.getOrderSide()).isEqualTo(OrderSide.SELL);
        }
    }

    @Test
    public void 지정가_매수_주문과_매칭되는_매도_주문_조회2() throws Exception{
        List<Order> matchingSellOrders = repository.findMatchingOrders(1l, OrderSide.BUY, OrderType.LIMIT, new BigDecimal(70000));
        assertThat(matchingSellOrders.size()).isEqualTo(2);
        for (Order sellOrder : matchingSellOrders) {
            assertThat(sellOrder.getOrderSide()).isEqualTo(OrderSide.SELL);
        }
    }

    @Test
    public void 시장가_매수_주문과_매칭되는_매도_주문_조회() throws Exception{
        List<Order> matchingSellOrders = repository.findMatchingOrders(1l, OrderSide.BUY, OrderType.MARKET, null);
        assertThat(matchingSellOrders.size()).isEqualTo(2);
        for (Order sellOrder : matchingSellOrders) {
            assertThat(sellOrder.getOrderSide()).isEqualTo(OrderSide.SELL);
        }
    }
}
