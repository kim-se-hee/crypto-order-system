package ksh.example.mybit.repository;

import ksh.example.mybit.domain.*;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


public interface OrderRepositoryCustom {

    Optional<Order> findMostPriorOrderByOrderTypeAndCoinId(OrderType orderType, Long coinId);

    Optional<Order> findMatchingOrder(Order order);;

    Long sumPendingOrderVolume(OrderSide orderSide, Member member, Coin coin);

    Optional<Order> findLatestOrder(Member member, Coin coin, OrderSide orderSide);

    List<Order> findPendingOrdersBy(Long memberId, Long coinId, Pageable pageable);

    void updateOrderStatusOfTriggeredOrders();
}
