package ksh.example.mybit.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import ksh.example.mybit.domain.Order;
import ksh.example.mybit.domain.OrderSide;
import ksh.example.mybit.domain.OrderStatus;
import ksh.example.mybit.domain.OrderType;

import java.math.BigDecimal;
import java.util.List;

import static ksh.example.mybit.domain.QOrder.*;

public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public OrderRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Order> findMatchingOrders(Long coinId, OrderSide orderSide, OrderType orderType, BigDecimal limitPrice) {
        return queryFactory
                .select(order)
                .from(order)
                .where(order.coin.id.eq(coinId),
                        oppositeOrderSide(orderSide),
                        order.orderType.eq(OrderType.LIMIT),
                        matchablePriceRange(orderSide, orderType, limitPrice),
                        order.orderStatus.eq(OrderStatus.PENDING))
                .orderBy(order.limitPrice.asc(), order.createdAt.asc())
                .fetch();
    }

    private BooleanExpression oppositeOrderSide(OrderSide orderSide) {
        if(orderSide == OrderSide.SELL) {
            return order.orderSide.eq(OrderSide.BUY);
        }

        return order.orderSide.eq(OrderSide.SELL);
    }

    private BooleanExpression matchablePriceRange(OrderSide orderSide, OrderType orderType, BigDecimal limitPrice) {
        if(orderType == OrderType.MARKET)
            return null;

        if(orderSide == OrderSide.BUY)
            return order.limitPrice.loe(limitPrice);

        return order.limitPrice.goe(limitPrice);
    }
}
