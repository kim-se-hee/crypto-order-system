package ksh.example.mybit.coin.controller;

import ksh.example.mybit.coin.domain.Coin;
import ksh.example.mybit.coin.service.dto.response.CoinListResponse;
import ksh.example.mybit.coin.service.CoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CoinController {

    private final CoinService coinService;

    @GetMapping("/coins")
    public ResponseEntity<CoinListResponse> marketList(Pageable pageable) {
        Page<Coin> page = coinService.getListedCoins(pageable);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CoinListResponse(page));
    }
}
