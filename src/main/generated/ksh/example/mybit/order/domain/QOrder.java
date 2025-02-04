package ksh.example.mybit.order.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrder is a Querydsl query type for Order
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrder extends EntityPathBase<Order> {

    private static final long serialVersionUID = 636795779L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOrder order = new QOrder("order1");

    public final ksh.example.mybit.coin.domain.QCoin coin;

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<java.math.BigDecimal> limitPrice = createNumber("limitPrice", java.math.BigDecimal.class);

    public final ksh.example.mybit.member.domain.QMember member;

    public final EnumPath<OrderSide> orderSide = createEnum("orderSide", OrderSide.class);

    public final EnumPath<OrderStatus> orderStatus = createEnum("orderStatus", OrderStatus.class);

    public final EnumPath<OrderType> orderType = createEnum("orderType", OrderType.class);

    public final NumberPath<java.math.BigDecimal> stopPrice = createNumber("stopPrice", java.math.BigDecimal.class);

    public final NumberPath<Integer> volume = createNumber("volume", Integer.class);

    public QOrder(String variable) {
        this(Order.class, forVariable(variable), INITS);
    }

    public QOrder(Path<? extends Order> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOrder(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOrder(PathMetadata metadata, PathInits inits) {
        this(Order.class, metadata, inits);
    }

    public QOrder(Class<? extends Order> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.coin = inits.isInitialized("coin") ? new ksh.example.mybit.coin.domain.QCoin(forProperty("coin")) : null;
        this.member = inits.isInitialized("member") ? new ksh.example.mybit.member.domain.QMember(forProperty("member")) : null;
    }

}

