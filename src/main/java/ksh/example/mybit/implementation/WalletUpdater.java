package ksh.example.mybit.implementation;

import ksh.example.mybit.domain.MemberCoin;
import ksh.example.mybit.domain.Order;
import ksh.example.mybit.domain.Trade;
import ksh.example.mybit.repository.MemberCoinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WalletUpdater {
    private final MemberCoinRepository memberCoinRepository;

    public void reflectMatchingResult(Trade trade) {
        updatePurchaserWallet(trade);
        updateSellerWallet(trade);
    }

    private void updatePurchaserWallet(Trade trade) {
        Order order = trade.getBuyOrder();

        MemberCoin purchasedCoin = memberCoinRepository
                .findWithWriteLockByMemberAndCoin(order.getMember(), order.getCoin())
                .orElseGet(() -> memberCoinRepository.save(
                        new MemberCoin(
                                null,
                                trade.getExecutedVolume().longValue(),
                                order.getMember(),
                                order.getCoin()
                        )
                ));

        purchasedCoin.increaseKoreanWonValue(trade.getExecutedVolume());

        memberCoinRepository
                .findWithWriteLockByMemberAndCoinTicker(order.getMember(), "won")
                .ifPresent(won -> won.decreaseKoreanWonValue(trade.getExecutedVolume()));
    }

    public void increaseAmountOf(MemberCoin memberCoin, Integer amount) {
        memberCoin.increaseKoreanWonValue(amount);
    }

    public void decreaseAmountOf(MemberCoin memberCoin, Integer amount) {
        memberCoin.decreaseKoreanWonValue(amount);
    }

    private void updateSellerWallet(Trade trade) {
        Order order = trade.getSellOrder();

        MemberCoin soldCoin = memberCoinRepository
                .findWithWriteLockByMemberAndCoin(order.getMember(), order.getCoin())
                .orElseThrow(() -> new IllegalArgumentException("지갑에 충분한 양의 코인이 없습니다."));
        soldCoin.decreaseKoreanWonValue(trade.getExecutedVolume());

        memberCoinRepository
                .findWithWriteLockByMemberAndCoinTicker(order.getMember(), "won")
                .ifPresent(won -> won.increaseKoreanWonValue(trade.getExecutedVolume()));
    }



}
