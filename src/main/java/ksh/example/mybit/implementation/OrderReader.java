package ksh.example.mybit.implementation;

import ksh.example.mybit.domain.*;
import ksh.example.mybit.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderReader {
    private final CoinSelector coinSelector;
    private final OrderRepository orderRepository;

    public Order readMostPriorOrder() {
        long coinId = coinSelector.getCurrentCoin();

        return orderRepository
                .findMostPriorOrderByOrderTypeAndCoinId(OrderType.MARKET, coinId)
                .orElseGet(() -> orderRepository
                        .findMostPriorOrderByOrderTypeAndCoinId(OrderType.LIMIT, coinId)
                        .orElseThrow(NoSuchElementException::new));


    }

    public Order readMatchingOrder(Order order) {
        return orderRepository
                .findMatchingOrder(order)
                .orElseThrow(NoSuchElementException::new);
    }

    public Optional<Order> readLatestOrderBy(Member member, Coin coin, OrderSide orderSide){
        return orderRepository.findLatestOrder(member, coin, orderSide);
    }
}
