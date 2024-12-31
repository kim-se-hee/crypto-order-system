package ksh.example.mybit.service;

import ksh.example.mybit.domain.Coin;
import ksh.example.mybit.domain.Member;
import ksh.example.mybit.domain.Order;
import ksh.example.mybit.domain.OrderSide;
import ksh.example.mybit.implementation.Validator;
import ksh.example.mybit.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final Validator validator;
    private final OrderRepository orderRepository;

    public Order placeOrder(Order order) {
        validator.checkOrderIsValid(order);

        orderRepository.save(order);
        return order;
    }

    public void checkTimeIntervalBetweenCurrentAndOrder(Member member, Coin coin, OrderSide orderSide) {
        orderRepository.findLatestOrder(member, coin, orderSide)
                .ifPresent(order -> {
                    if(calculateTimeInterval(order) < 5000){
                        throw new IllegalStateException("너무 자주 요청했습니다.");
                    }
                });
    }

    private static long calculateTimeInterval(Order order) {
        return Duration.between(order.getCreatedAt(), LocalDateTime.now()).toMillis();
    }
}
