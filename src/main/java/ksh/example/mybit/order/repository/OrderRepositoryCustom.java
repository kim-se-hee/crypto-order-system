package ksh.example.mybit.order.repository;

import ksh.example.mybit.coin.domain.Coin;
import ksh.example.mybit.member.domain.Member;
import ksh.example.mybit.order.domain.Order;
import ksh.example.mybit.order.domain.OrderSide;
import ksh.example.mybit.order.domain.OrderType;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


public interface OrderRepositoryCustom {

    Optional<Order> findMostPriorOrderByOrderTypeAndCoinId(OrderType orderType, Long coinId);

    Optional<Order> findMatchingOrder(Order order);

    Long sumPendingOrderVolume(OrderSide orderSide, Long memberId, Long coinId);

    Optional<Order> findLatestOrder(Long memberId, Long coinId, OrderSide orderSide);

    List<Order> findPendingOrdersBy(Long memberId, Long coinId, Pageable pageable);

    void updateOrderStatusOfTriggeredOrders();
}
