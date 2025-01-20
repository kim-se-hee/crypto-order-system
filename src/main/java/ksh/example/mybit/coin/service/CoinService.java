package ksh.example.mybit.coin.service;

import ksh.example.mybit.coin.domain.Coin;
import ksh.example.mybit.coin.repository.CoinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CoinService {
    private final CoinRepository coinRepository;

    public Coin findCoin(Long id) {
        return coinRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 코인입니다."));
    }

    public Page<Coin> getListedCoins(Pageable pageable) {
        return coinRepository.findAll(pageable);
    }
}
