package ksh.example.mybit.controller.dto;

import ksh.example.mybit.domain.Coin;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@Setter
public class CoinDto {

    private Long id;
    private String name;
    private String ticker;
    private BigDecimal price;
    private BigDecimal tick;
    private BigDecimal changeRate;

    public CoinDto(Coin coin) {
        this.id = coin.getId();
        this.name = coin.getName();
        this.ticker = coin.getTicker();
        this.price = coin.getPrice();
        this.tick = coin.getTick();
        this.changeRate = coin.getClosingPrice().subtract(coin.getPrice()).divide(coin.getClosingPrice(), 2, RoundingMode.HALF_UP);
    }
}
