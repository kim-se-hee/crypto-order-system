package ksh.example.mybit.persistence.mysql.jpa.repository;

import ksh.example.mybit.persistence.mysql.jpa.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {
}
