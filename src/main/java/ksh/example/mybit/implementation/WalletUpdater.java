package ksh.example.mybit.implementation;

import ksh.example.mybit.domain.MemberCoin;
import ksh.example.mybit.domain.Order;
import ksh.example.mybit.domain.Trade;
import ksh.example.mybit.repository.MemberCoinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class WalletUpdater {
    private final MemberCoinRepository memberCoinRepository;

    public void updateWallet(Trade trade) {
        updatePurchaserWallet(trade);
        updateSellerWallet(trade);
    }

    private void updatePurchaserWallet(Trade trade) {
        Order order = trade.getBuyOrder();
        Optional<MemberCoin> optionalMemberCoin = memberCoinRepository.findByMemberAndCoin(order.getMember(), order.getCoin());

        if (optionalMemberCoin.isEmpty()) {
            memberCoinRepository.save(
                    new MemberCoin(
                            null,
                            trade.getExecutedAmount().longValue(),
                            order.getMember(),
                            order.getCoin())
            );
            return;
        }

        MemberCoin memberCoin = optionalMemberCoin.get();
        memberCoin.increaseKoreanWonValue(trade.getExecutedAmount());
    }

    private void updateSellerWallet(Trade trade) {
        Order order = trade.getSellOrder();
        Optional<MemberCoin> optionalMemberCoin = memberCoinRepository.findByMemberAndCoin(order.getMember(), order.getCoin());

        if (optionalMemberCoin.isEmpty()) {
            throw new IllegalArgumentException("지갑에 충분한 양의 코인이 없습니다.");
        }

        MemberCoin memberCoin = optionalMemberCoin.get();
        memberCoin.decreaseKoreanWonValue(trade.getExecutedAmount());
    }
}
