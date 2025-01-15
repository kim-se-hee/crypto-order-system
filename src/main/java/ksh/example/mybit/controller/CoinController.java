package ksh.example.mybit.controller;

import ksh.example.mybit.controller.dto.CoinListResponseDto;
import ksh.example.mybit.domain.Coin;
import ksh.example.mybit.service.CoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CoinController {

    private final CoinService coinService;

    @GetMapping("/coins")
    public CoinListResponseDto marketList(Pageable pageable) {
        Page<Coin> page = coinService.getListedCoins(pageable);
        return new CoinListResponseDto(page);
    }
}
