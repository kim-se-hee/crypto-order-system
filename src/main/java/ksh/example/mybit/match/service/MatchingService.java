package ksh.example.mybit.match.service;

import ksh.example.mybit.coin.implementaion.CoinUpdater;
import ksh.example.mybit.match.implementation.OrderMatcher;
import ksh.example.mybit.membercoin.implementation.WalletUpdater;
import ksh.example.mybit.order.domain.Order;
import ksh.example.mybit.order.implementation.OrderReader;
import ksh.example.mybit.trade.domain.Trade;
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
