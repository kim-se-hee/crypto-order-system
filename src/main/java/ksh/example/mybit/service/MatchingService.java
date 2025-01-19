package ksh.example.mybit.service;

import ksh.example.mybit.domain.Order;
import ksh.example.mybit.domain.Trade;
import ksh.example.mybit.implementation.CoinUpdater;
import ksh.example.mybit.implementation.OrderMatcher;
import ksh.example.mybit.implementation.OrderReader;
import ksh.example.mybit.implementation.WalletUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MatchingService {

    private final OrderReader orderReader;
    private final OrderMatcher orderMatcher;
    private final WalletUpdater walletUpdater;
    private final CoinUpdater coinUpdater;

    @Transactional
    public Trade matchOrder() {
        Order order = orderReader.readMostPriorOrder();
        Order matchingOrder = orderReader.readMatchingOrder(order);

        Trade executedTrade = orderMatcher.match(order, matchingOrder);

        walletUpdater.reflectMatchingResult(executedTrade);

        coinUpdater.updatePrice(order.getCoin(), executedTrade.getExecutedPrice());

        return executedTrade;
    }
}
