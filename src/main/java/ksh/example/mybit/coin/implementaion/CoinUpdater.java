package ksh.example.mybit.coin.implementaion;

import ksh.example.mybit.coin.domain.Coin;
import ksh.example.mybit.coin.repository.CoinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CoinUpdater {

    private final CoinRepository coinRepository;

    public void updatePrice(Coin coin, BigDecimal price) {
        coin.updatePrice(price);
    }

}
