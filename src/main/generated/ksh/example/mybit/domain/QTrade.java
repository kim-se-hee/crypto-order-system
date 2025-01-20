package ksh.example.mybit.domain2;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTrade is a Querydsl query type for Trade
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTrade extends EntityPathBase<Trade> {

    private static final long serialVersionUID = -423074247L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTrade trade = new QTrade("trade");

    public final QOrder buyOrder;

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Integer> executedAmount = createNumber("executedAmount", Integer.class);

    public final NumberPath<java.math.BigDecimal> executedPrice = createNumber("executedPrice", java.math.BigDecimal.class);

    public final NumberPath<Long> executedQuantity = createNumber("executedQuantity", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QOrder sellOrder;

    public QTrade(String variable) {
        this(Trade.class, forVariable(variable), INITS);
    }

    public QTrade(Path<? extends Trade> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTrade(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTrade(PathMetadata metadata, PathInits inits) {
        this(Trade.class, metadata, inits);
    }

    public QTrade(Class<? extends Trade> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.buyOrder = inits.isInitialized("buyOrder") ? new QOrder(forProperty("buyOrder"), inits.get("buyOrder")) : null;
        this.sellOrder = inits.isInitialized("sellOrder") ? new QOrder(forProperty("sellOrder"), inits.get("sellOrder")) : null;
    }

}

