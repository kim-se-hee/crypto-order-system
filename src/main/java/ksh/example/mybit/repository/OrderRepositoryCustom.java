package ksh.example.mybit.repository;

import ksh.example.mybit.domain.Order;
import ksh.example.mybit.domain.OrderSide;
import ksh.example.mybit.domain.OrderType;

import java.math.BigDecimal;
import java.util.List;

public interface OrderRepositoryCustom {

    List<Order> findMatchingOrders(Long coinId, OrderSide orderSide, OrderType orderType, BigDecimal limitPrice);
}
