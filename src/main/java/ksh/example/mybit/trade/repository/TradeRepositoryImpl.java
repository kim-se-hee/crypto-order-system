package ksh.example.mybit.trade.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import ksh.example.mybit.trade.domain.Trade;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static ksh.example.mybit.trade.domain.QTrade.*;

public class TradeRepositoryImpl implements TradeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public TradeRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Trade> findByMemberIdAndCoinId(Long memberId, Long coinId, Pageable pageable) {
        return queryFactory
                .select(trade)
                .from(trade)
                .where(
                        trade.buyOrder.member.id.eq(memberId).and(trade.buyOrder.coin.id.eq(coinId))
                                .or(trade.sellOrder.member.id.eq(memberId).and(trade.sellOrder.coin.id.eq(coinId)))

                )
                .orderBy(trade.createdAt.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
}
