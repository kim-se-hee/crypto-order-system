package ksh.example.mybit.service;

import ksh.example.mybit.domain.Coin;
import ksh.example.mybit.repository.CoinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CoinService {
    private final CoinRepository coinRepository;

    public Coin findCoin(Long id) {
        return coinRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 코인입니다."));
    }
}
