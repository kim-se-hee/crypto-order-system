package ksh.example.mybit.order.implementation;

import ksh.example.mybit.membercoin.domain.MemberCoin;
import ksh.example.mybit.membercoin.implementation.WalletReader;
import ksh.example.mybit.order.domain.Order;
import ksh.example.mybit.order.domain.OrderSide;
import ksh.example.mybit.order.domain.OrderStatus;
import ksh.example.mybit.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class OrderValidator {

    private final WalletReader walletReader;

    private final OrderRepository orderRepository;


    public void checkTimeIntervalFromLatestOrder(Long memberId, Long coinId, OrderSide orderSide) {
        orderRepository.findLatestOrder(memberId, coinId, orderSide)
                .ifPresent(o -> {
                    if (calculateTimeInterval(o) < 5000) {
                        throw new IllegalStateException("너무 자주 요청했습니다.");
                    }
                });
    }

    public void checkOrderIsPending(Long orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        if(order.getOrderStatus() != OrderStatus.PENDING) {
            throw new IllegalArgumentException("이미 종료된 주문입니다.");
        }
    }

    public void checkOrderVolumeIsValid(Long memberId, Long coinId, Integer volume, OrderSide orderSide) {

        if (orderSide == OrderSide.SELL) {
            checkAvailableCoinBalance(memberId, coinId, volume);
            return;
        }

        checkAvailableKoreanWonBalance(memberId, coinId, volume);
    }

    private static long calculateTimeInterval(Order order) {
        return Duration.between(order.getCreatedAt(), LocalDateTime.now()).toMillis();
    }

    private void checkAvailableCoinBalance(Long memberId, Long coinId, Integer volume) {

        MemberCoin memberCoin = walletReader.readByMemberIdAndCoinId(memberId, coinId);

        Long pendingVolume = orderRepository.sumPendingOrderVolume(OrderSide.SELL, memberId, coinId);
        long availableBalance = memberCoin.getBalance() - pendingVolume;

        if (availableBalance < volume) {
            throw new IllegalArgumentException("주문 가능한 수량이 부족합니다");
        }
    }

    private void checkAvailableKoreanWonBalance(Long memberId, Long coinId, Integer volume) {

        MemberCoin memberCoin = walletReader.readByMemberIdAndCoinId(memberId, coinId);

        Long pendingVolume = orderRepository.sumPendingOrderVolume(OrderSide.BUY, memberId, null);
        long availableBalance = memberCoin.getBalance() - pendingVolume;

        if (availableBalance < volume) {
            throw new IllegalArgumentException("주문 가능한 금액이 부족합니다");
        }
    }

}
