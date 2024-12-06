package ksh.example.mybit.service;

import ksh.example.mybit.domain.*;
import ksh.example.mybit.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class MatchingServiceTest {

    @Autowired MatchingService matchingService;
    @Autowired OrderRepository orderRepository;

    @Test
    public void 매칭_결과_테스트() throws Exception{
        matchingService.matchOrder();

        List<Order> after = orderRepository.findAll();
        List<Order> finished = after.stream()
                .filter(o -> o.getOrderStatus().equals(OrderStatus.FINISHED))
                .toList();

        List<Order> remain = after.stream()
                .filter(o -> o.getOrderStatus().equals(OrderStatus.PENDING))
                .toList();

        assertThat(finished.size()).isEqualTo(4);
        assertThat(remain.size()).isEqualTo(1);

        for (Order order : finished) {
            assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.FINISHED);
            assertThat(order.getAmount()).isEqualTo(0);
        }

        for (Order order : remain) {
            assertThat(order.getOrderStatus()).isNotEqualTo(OrderStatus.FINISHED);
            assertThat(order.getAmount()).isEqualTo(500);
        }
    }

}
