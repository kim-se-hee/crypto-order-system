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

    public void increaseQuantityOf(MemberCoin memberCoin, BigDecimal quantity) {
        memberCoin.increaseQuantity(quantity);
    }

    public void decreaseBalanceOf(MemberCoin memberCoin, BigDecimal quantity) {
        memberCoin.decreaseQuantity(quantity);
    }

    private void updatePurchaserWallet(Trade trade) {
        Order order = trade.getBuyOrder();

        MemberCoin purchasedCoin = memberCoinRepository
                .findWithWriteLockByMemberAndCoin(order.getMember(), order.getCoin())
                .orElseGet(() -> memberCoinRepository.save(
                        MemberCoin.builder()
                                .quantity(BigDecimal.ZERO)
                                .member(order.getMember())
                                .coin(order.getCoin())
                                .build()
                ));

        BigDecimal updatedAveragePrice = PortfolioAnalyzer.calculateAveragePrice(purchasedCoin, trade.getExecutedPrice(), trade.getExecutedQuantity());
        purchasedCoin.updateAveragePrice(updatedAveragePrice);
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
