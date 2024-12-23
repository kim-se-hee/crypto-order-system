package ksh.example.mybit.repository;

import com.querydsl.core.types.NullExpression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import ksh.example.mybit.domain.*;

import java.math.BigDecimal;
import java.util.Optional;

import static ksh.example.mybit.domain.QOrder.order;

public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public OrderRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<Order> findMostPriorOrderByOrderTypeAndCoinId(OrderType orderType, Long coinId) {
        Order findOrder = queryFactory
                .select(order)
                .from(order)
                .where(
                        order.orderStatus.eq(OrderStatus.PENDING),
                        order.orderType.eq(orderType),
                        orderSideEquals(orderType),
                        coinIdEquals(orderType, coinId)
                )
                .orderBy(
                        sortEntireOrderByLimitPrice(orderType),
                        order.createdAt.asc(),
                        order.amount.desc()
                )
                .fetchFirst();

        return Optional.ofNullable(findOrder);
    }


    @Override
    public Optional<Order> findMatchingOrder(Order o) {
        Order findOrder = queryFactory
                .select(order)
                .from(order)
                .where(
                        order.member.id.ne(o.getMember().getId()),
                        order.coin.id.eq(o.getCoin().getId()),
                        order.orderSide.ne(o.getOrderSide()),
                        order.orderStatus.eq(OrderStatus.PENDING),
                        order.orderType.eq(OrderType.LIMIT).and(
                                matchablePriceRange(o.getOrderSide(), o.getOrderType(), o.getLimitPrice()))
                )
                .orderBy(
                        sortMatchableOrderByLimitPrice(o.getOrderSide()),
                        order.createdAt.asc(),
                        order.amount.desc()
                )
                .fetchFirst();

        return Optional.ofNullable(findOrder);
    }

    @Override
    public Long sumPendingOrderAmount(OrderSide orderSide, Member member, Coin coin) {
        Integer sum = queryFactory
                .select(order.amount.sum())
                .from(order)
                .where(
                        order.member.eq(member),
                        coin == null ? null : order.coin.eq(coin),
                        order.orderSide.eq(orderSide),
                        order.orderStatus.eq(OrderStatus.PENDING))
                .fetchOne();

        if(sum == null)
            return 0l;

        return sum.longValue();
    }

    private BooleanExpression coinIdEquals(OrderType orderType, Long coinId) {
        if(orderType == OrderType.MARKET)
            return null;

        return order.coin.id.eq(coinId);
    }

    private BooleanExpression orderSideEquals(OrderType orderType) {
        if (orderType == OrderType.MARKET)
            return null;

        return order.orderSide.eq(OrderSide.BUY);
    }

    private OrderSpecifier<BigDecimal> sortEntireOrderByLimitPrice(OrderType orderType) {
        if (orderType == OrderType.MARKET)
            return new OrderSpecifier(null, NullExpression.DEFAULT, OrderSpecifier.NullHandling.Default);

        return order.limitPrice.desc();
    }

    private BooleanExpression matchablePriceRange(OrderSide orderSide, OrderType orderType, BigDecimal limitPrice) {
        if (orderType == OrderType.MARKET)
            return null;

        if (orderSide == OrderSide.BUY)
            return order.limitPrice.loe(limitPrice);

        return order.limitPrice.goe(limitPrice);
    }


    private OrderSpecifier<BigDecimal> sortMatchableOrderByLimitPrice(OrderSide oppositeOrderSide) {
        if (oppositeOrderSide == OrderSide.SELL)
            return order.limitPrice.desc();

        return order.limitPrice.asc();
    }

}
