package ksh.example.mybit.service;

import ksh.example.mybit.domain.Order;
import ksh.example.mybit.domain.OrderStatus;
import ksh.example.mybit.domain.Trade;
import ksh.example.mybit.implementation.OrderMatcher;
import ksh.example.mybit.implementation.WalletUpdater;
import ksh.example.mybit.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MatchingService {
    private final OrderRepository orderRepository;

    private final OrderMatcher orderMatcher;
    private final WalletUpdater walletUpdater;

    @Transactional
    public Optional<Trade> matchOrder() {
        Order order = orderRepository.findFirstByOrderStatusOrderByCreatedAtAsc(OrderStatus.PENDING);
        Order matchingOrder = orderRepository.findMatchingOrder(order);
        if (matchingOrder == null) {
            return Optional.empty();
        }

        Trade executedTrade = orderMatcher.match(order, matchingOrder);

        walletUpdater.updateWallet(executedTrade);

        return Optional.of(executedTrade);
    }
}
