package ksh.example.mybit.service;

import ksh.example.mybit.domain.Order;
import ksh.example.mybit.implementation.Validator;
import ksh.example.mybit.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final Validator validator;
    private final OrderRepository orderRepository;

    public Order placeOrder(Order order) {
        validator.checkOrderIsValid(order);

        orderRepository.save(order);
        return order;
    }

}
