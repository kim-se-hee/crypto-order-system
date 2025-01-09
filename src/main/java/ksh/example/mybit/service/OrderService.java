package ksh.example.mybit.service;

import ksh.example.mybit.domain.Order;
import ksh.example.mybit.implementation.OrderWriter;
import ksh.example.mybit.implementation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final Validator validator;
    private final OrderWriter orderWriter;

    @Transactional
    public Order placeOrder(Order order) {
        validator.checkTimeIntervalFromLatestOrder(order);

        validator.checkOrderIsValid(order);

        orderWriter.save(order);
        return order;
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        validator.checkOrderIsPending(orderId);

        orderWriter.cancel(orderId);
    }
}
