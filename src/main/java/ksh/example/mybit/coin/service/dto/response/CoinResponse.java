package ksh.example.mybit.coin.service.dto.response;

import ksh.example.mybit.coin.domain.Coin;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@Setter
public class CoinResponse {

    private Long id;
    private String name;
    private String ticker;
    private BigDecimal price;
    private BigDecimal tick;
    private BigDecimal changeRate;

    public CoinResponse(Coin coin) {
        this.id = coin.getId();
        this.name = coin.getName();
        this.ticker = coin.getTicker();
        this.price = coin.getPrice();
        this.tick = coin.getTick();
        this.changeRate = coin.getChangeRate();
    }
}
