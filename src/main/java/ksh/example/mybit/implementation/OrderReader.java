package ksh.example.mybit.implementation;

import ksh.example.mybit.domain.Order;
import ksh.example.mybit.domain.OrderType;
import ksh.example.mybit.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class OrderReader {
    private final OrderRepository orderRepository;

    public Order readMostPriorOrder() {
        return orderRepository
                .findMostPriorOrderTypeOf(OrderType.MARKET)
                .orElseGet(() -> orderRepository
                        .findMostPriorOrderTypeOf(OrderType.LIMIT)
                        .orElseThrow(NoSuchElementException::new));


    }

    public Order readMatchingOrder(Order order) {
        return orderRepository
                .findMatchingOrder(order)
                .orElseThrow(NoSuchElementException::new);
    }
}
