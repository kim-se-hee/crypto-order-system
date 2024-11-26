package ksh.example.mybit.repository;

import ksh.example.mybit.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
