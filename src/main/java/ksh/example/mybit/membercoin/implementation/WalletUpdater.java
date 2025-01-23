package ksh.example.mybit.membercoin.implementation;

import ksh.example.mybit.membercoin.domain.MemberCoin;
import ksh.example.mybit.membercoin.repository.MemberCoinRepository;
import ksh.example.mybit.order.domain.Order;
import ksh.example.mybit.trade.domain.Trade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class WalletUpdater {
    private final MemberCoinRepository memberCoinRepository;

    public void reflectMatchingResult(Trade trade) {
        updatePurchaserWallet(trade);
        updateSellerWallet(trade);
    }

    public void increaseBalanceOf(MemberCoin memberCoin, Integer balance) {
        memberCoin.increaseBalance(balance);
    }

    public void decreaseBalanceOf(MemberCoin memberCoin, Integer balance) {
        memberCoin.decreaseBalance(balance);
    }

    private void updatePurchaserWallet(Trade trade) {
        Order order = trade.getBuyOrder();

        MemberCoin purchasedCoin = memberCoinRepository
                .findWithWriteLockByMemberAndCoin(order.getMember(), order.getCoin())
                .orElseGet(() -> memberCoinRepository.save(
                        MemberCoin.builder()
                                .quantity(BigDecimal.ZERO)
                                .balance(trade.getExecutedVolume().longValue())
                                .member(order.getMember())
                                .coin(order.getCoin())
                                .build()
                ));

        purchasedCoin.increaseQuantity(trade.getExecutedQuantity());

        memberCoinRepository
                .findWithWriteLockByMemberAndCoinTicker(order.getMember(), "won")
                .ifPresent(won -> won.decreaseQuantity(new BigDecimal(trade.getExecutedVolume())));
    }

    private void updateSellerWallet(Trade trade) {
        Order order = trade.getSellOrder();

        MemberCoin soldCoin = memberCoinRepository
                .findWithWriteLockByMemberAndCoin(order.getMember(), order.getCoin())
                .orElseThrow(() -> new IllegalArgumentException("지갑에 충분한 양의 코인이 없습니다."));
        soldCoin.decreaseQuantity(trade.getExecutedQuantity());

        memberCoinRepository
                .findWithWriteLockByMemberAndCoinTicker(order.getMember(), "won")
                .ifPresent(won -> won.increaseQuantity(new BigDecimal(trade.getExecutedVolume())));
    }


}
