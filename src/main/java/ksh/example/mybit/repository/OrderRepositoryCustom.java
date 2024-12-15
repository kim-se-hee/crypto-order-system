package ksh.example.mybit.repository;

import ksh.example.mybit.domain.Order;
import ksh.example.mybit.domain.OrderType;

import java.util.Optional;


public interface OrderRepositoryCustom {

    Optional<Order> findMostPriorOrderTypeOf(OrderType orderType);

    Optional<Order> findMatchingOrder(Order order);

}
