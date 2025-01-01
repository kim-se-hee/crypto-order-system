package ksh.example.mybit.implementation;

import ksh.example.mybit.domain.Order;
import ksh.example.mybit.repository.OrderRepository;
import ksh.example.mybit.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderWriter {

    private final OrderRepository orderRepository;

    public Order save(Order order) {
        return orderRepository.save(order);
    }
}
