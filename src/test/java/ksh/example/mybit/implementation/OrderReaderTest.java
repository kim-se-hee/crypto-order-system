package ksh.example.mybit.implementation;

import ksh.example.mybit.domain.Order;
import ksh.example.mybit.domain.OrderSide;
import ksh.example.mybit.domain.OrderType;
import ksh.example.mybit.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class OrderReaderTest {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderReader orderReader;

    @Test
    public void 지정가만_존재_가장_높은_지정가_매수가_조회() throws Exception {
        Order order1 = new Order(1000, OrderSide.BUY, OrderType.LIMIT, new BigDecimal(62000), null, null);
        Order order2 = new Order(1000, OrderSide.BUY, OrderType.LIMIT, new BigDecimal(61000), null, null);
        orderRepository.save(order1);
        orderRepository.save(order2);

        Order order = orderReader.readMostPriorOrder();
        assertThat(order.getId()).isEqualTo(order1.getId());
    }

    @Test
    public void 지정가만_존재_가장_일찍_생성된_지정가_매수가_조회() throws Exception {
        Order order1 = new Order(1000, OrderSide.BUY, OrderType.LIMIT, new BigDecimal(62000), null, null);
        Order order2 = new Order(1000, OrderSide.BUY, OrderType.LIMIT, new BigDecimal(62000), null, null);
        orderRepository.save(order1);
        orderRepository.save(order2);

        Order order = orderReader.readMostPriorOrder();
        assertThat(order.getId()).isEqualTo(order1.getId());
    }

    @Test
    public void 시장가만_존재_가장_일찍_생성된_시장가_조회() throws Exception {
        Order order1 = new Order(1000, OrderSide.SELL, OrderType.MARKET, null, null, null);
        Order order2 = new Order(1000, OrderSide.BUY, OrderType.MARKET, null, null, null);
        orderRepository.save(order1);
        orderRepository.save(order2);

        Order order = orderReader.readMostPriorOrder();
        assertThat(order.getId()).isEqualTo(order1.getId());
    }

    @Test
    public void 시장가_지정가_혼재_시장가가_조회() throws Exception {
        Order order1 = new Order(1000, OrderSide.BUY, OrderType.LIMIT, new BigDecimal(6461), null, null);
        Order order2 = new Order(1000, OrderSide.SELL, OrderType.MARKET, null, null, null);
        orderRepository.save(order1);
        orderRepository.save(order2);

        Order order = orderReader.readMostPriorOrder();
        assertThat(order.getId()).isEqualTo(order2.getId());
    }
}
