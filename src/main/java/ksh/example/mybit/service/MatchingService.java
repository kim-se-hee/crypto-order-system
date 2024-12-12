package ksh.example.mybit.service;

import ksh.example.mybit.domain.Trade;
import ksh.example.mybit.implementation.OrderMatcher;
import ksh.example.mybit.implementation.WalletUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MatchingService {

    private final OrderMatcher orderMatcher;
    private final WalletUpdater walletUpdater;

    @Transactional
    public Optional<Trade> matchOrder() {
        Optional<Trade> optionalTrade = orderMatcher.match();
        optionalTrade.ifPresent(walletUpdater::updateWallet);
        return optionalTrade;
    }
}
