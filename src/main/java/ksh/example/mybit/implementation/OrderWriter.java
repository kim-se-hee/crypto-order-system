package ksh.example.mybit.implementation;

import ksh.example.mybit.domain.Order;
import ksh.example.mybit.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderWriter {

    private final OrderRepository orderRepository;

    public Order save(Order order) {
        return orderRepository.save(order);
    }

    public void cancel(Long orderId) {
        orderRepository.findById(orderId)
                .ifPresent(Order::cancel);
    }

    public void activateTriggeredOrders(){
        orderRepository.updateOrderStatusOfTriggeredOrders();
    }
}
