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

        purchasedCoin.increaseBalance(trade.getExecutedVolume());

        memberCoinRepository
                .findWithWriteLockByMemberAndCoinTicker(order.getMember(), "won")
                .ifPresent(won -> won.decreaseBalance(trade.getExecutedVolume()));
    }

    public void increaseBalanceOf(MemberCoin memberCoin, Integer balance) {
        memberCoin.increaseBalance(balance);
    }

    public void decreaseBalanceOf(MemberCoin memberCoin, Integer balance) {
        memberCoin.decreaseBalance(balance);
    }

    private void updateSellerWallet(Trade trade) {
        Order order = trade.getSellOrder();

        MemberCoin soldCoin = memberCoinRepository
                .findWithWriteLockByMemberAndCoin(order.getMember(), order.getCoin())
                .orElseThrow(() -> new IllegalArgumentException("지갑에 충분한 양의 코인이 없습니다."));
        soldCoin.decreaseBalance(trade.getExecutedVolume());

        memberCoinRepository
                .findWithWriteLockByMemberAndCoinTicker(order.getMember(), "won")
                .ifPresent(won -> won.increaseBalance(trade.getExecutedVolume()));
    }



}
