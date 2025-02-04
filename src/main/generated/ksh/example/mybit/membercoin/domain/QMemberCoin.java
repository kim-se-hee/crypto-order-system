package ksh.example.mybit.membercoin.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberCoin is a Querydsl query type for MemberCoin
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberCoin extends EntityPathBase<MemberCoin> {

    private static final long serialVersionUID = 1430234481L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberCoin memberCoin = new QMemberCoin("memberCoin");

    public final NumberPath<java.math.BigDecimal> averagePrice = createNumber("averagePrice", java.math.BigDecimal.class);

    public final ksh.example.mybit.coin.domain.QCoin coin;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ksh.example.mybit.member.domain.QMember member;

    public final NumberPath<java.math.BigDecimal> quantity = createNumber("quantity", java.math.BigDecimal.class);

    public QMemberCoin(String variable) {
        this(MemberCoin.class, forVariable(variable), INITS);
    }

    public QMemberCoin(Path<? extends MemberCoin> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberCoin(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberCoin(PathMetadata metadata, PathInits inits) {
        this(MemberCoin.class, metadata, inits);
    }

    public QMemberCoin(Class<? extends MemberCoin> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.coin = inits.isInitialized("coin") ? new ksh.example.mybit.coin.domain.QCoin(forProperty("coin")) : null;
        this.member = inits.isInitialized("member") ? new ksh.example.mybit.member.domain.QMember(forProperty("member")) : null;
    }

}

