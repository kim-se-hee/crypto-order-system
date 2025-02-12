package ksh.example.mybit.order.implementation;


import ksh.example.mybit.global.util.CoinSelector;
import ksh.example.mybit.order.domain.Order;
import ksh.example.mybit.order.domain.OrderType;
import ksh.example.mybit.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class OrderReader {
    private final OrderRepository orderRepository;

    public Order readMostPriorOrder(long coinId) {
        return orderRepository
                .findMostPriorMarketOrderBy(coinId)
                .orElseGet(() -> orderRepository
                        .findMostPriorLimitOrderBy(coinId)
                        .orElseThrow(NoSuchElementException::new));


    }

    public Order readMatchingOrder(Order order) {
        return orderRepository
                .findMatchingOrder(order)
                .orElseThrow(NoSuchElementException::new);
    }

    public List<Order> readPendingOrdersBy(Long memberId, Long coinId, Pageable pageable) {
        return orderRepository.findPendingOrdersBy(memberId, coinId, pageable);
    }
}
