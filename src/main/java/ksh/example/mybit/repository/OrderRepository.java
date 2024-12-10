package ksh.example.mybit.repository;

import ksh.example.mybit.domain.Order;
import ksh.example.mybit.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {

    Order findFirstByOrderStatusOrderByCreatedAtAsc(OrderStatus status);

}
