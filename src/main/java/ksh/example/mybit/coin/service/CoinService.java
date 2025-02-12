package ksh.example.mybit.coin.service;

import ksh.example.mybit.coin.domain.Coin;
import ksh.example.mybit.coin.implementaion.CoinReader;
import ksh.example.mybit.coin.repository.CoinRepository;
import ksh.example.mybit.coin.service.dto.response.CoinListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CoinService {
    private final CoinReader coinReader;

    public Coin getById(Long id) {
        return coinReader.readById(id);
    }

    public CoinListResponse getListedCoins(Pageable pageable) {
        Page<Coin> page = coinReader.readAll(pageable);
        return new CoinListResponse(page);
    }
}
