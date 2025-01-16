package ksh.example.mybit.implementation.updater;

import ksh.example.mybit.persistence.mysql.jpa.entity.Order;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Theo
 * @since 2025/01/16
 */
public class OrderUpdater {
    @Transactional
    public void markAsDelete(long orderId) {
        orderRepository.findById(orderId)
                .ifPresent(Order::cancel);
    }
}
