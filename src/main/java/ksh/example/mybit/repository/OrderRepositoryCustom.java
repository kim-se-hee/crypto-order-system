package ksh.example.mybit.repository;

import ksh.example.mybit.domain.*;

import java.util.Optional;


public interface OrderRepositoryCustom {

    Optional<Order> findMostPriorOrderByOrderTypeAndCoinId(OrderType orderType, Long coinId);

    Optional<Order> findMatchingOrder(Order order);;

    Long sumPendingOrderAmount(OrderSide orderSide, Member member, Coin coin);

    Optional<Order> findLatestOrder(Member member, Coin coin, OrderSide orderSide);
}
