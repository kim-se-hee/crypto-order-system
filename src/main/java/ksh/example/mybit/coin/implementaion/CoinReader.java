package ksh.example.mybit.coin.implementaion;

import ksh.example.mybit.coin.domain.Coin;
import ksh.example.mybit.coin.repository.CoinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CoinReader {

    private final CoinRepository coinRepository;

    public Coin readById(Long id){
        return coinRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 자산입니다"));
    }
}
