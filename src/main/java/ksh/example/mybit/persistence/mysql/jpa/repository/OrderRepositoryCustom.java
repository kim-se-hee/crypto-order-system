package ksh.example.mybit.persistence.mysql.jpa.repository;

import ksh.example.mybit.persistence.mysql.jpa.entity.Coin;
import ksh.example.mybit.persistence.mysql.jpa.entity.Member;
import ksh.example.mybit.persistence.mysql.jpa.entity.Order;
import ksh.example.mybit.persistence.mysql.jpa.entity.type.OrderSide;
import ksh.example.mybit.persistence.mysql.jpa.entity.type.OrderType;

import java.util.Optional;


public interface OrderRepositoryCustom {

    Optional<Order> findMostPriorOrderByOrderTypeAndCoinId(OrderType orderType, Long coinId);

    Optional<Order> findMatchingOrder(Order order);;

    Long sumPendingOrderAmount(OrderSide orderSide, Member member, Coin coin);

    Optional<Order> findLatestOrder(Member member, Coin coin, OrderSide orderSide);
}
