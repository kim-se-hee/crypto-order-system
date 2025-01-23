package ksh.example.mybit.membercoin.implementation;

import ksh.example.mybit.membercoin.domain.MemberCoin;
import ksh.example.mybit.membercoin.repository.MemberCoinRepository;
import ksh.example.mybit.order.domain.Order;
import ksh.example.mybit.trade.domain.Trade;
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
                        MemberCoin.builder()
                                .quantity(trade.getExecutedQuantity())
                                .balance(trade.getExecutedVolume().longValue())
                                .member(order.getMember())
                                .coin(order.getCoin())
                                .build()
                ));

        purchasedCoin.increaseBalance(trade.getExecutedVolume());

        //여기 코인 티커로 찾는 거 없어도 됨
        //왜냐면 클라이언트 측에서 아이디를 보낼 거임
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
