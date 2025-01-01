package ksh.example.mybit.service;

import ksh.example.mybit.domain.Coin;
import ksh.example.mybit.domain.Member;
import ksh.example.mybit.domain.Order;
import ksh.example.mybit.domain.OrderSide;
import ksh.example.mybit.implementation.OrderReader;
import ksh.example.mybit.implementation.OrderWriter;
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
    private final OrderWriter orderWriter;

    public Order placeOrder(Order order) {
        validator.checkTimeIntervalFromLatestOrder(order);

        validator.checkOrderIsValid(order);

        orderWriter.save(order);
        return order;
    }

}
