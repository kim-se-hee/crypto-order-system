package ksh.example.mybit.coin.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Random;

@Entity
@Getter
public class Coin {
    @Id
    @Column(name = "coin_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String ticker;

    private BigDecimal price;

    private BigDecimal tick;

    private BigDecimal closingPrice;

    private BigDecimal previousPrice;

    @Builder
    private Coin(String name, String ticker, BigDecimal price, BigDecimal tick, BigDecimal closingPrice, BigDecimal previousPrice) {
        this.name = name;
        this.ticker = ticker;
        this.price = price;
        this.tick = tick;
        this.closingPrice = closingPrice;
        this.previousPrice = previousPrice;
    }

    public Coin() {
    }

    public void updatePrice(BigDecimal price) {
        this.previousPrice = this.price;
        this.price = price;
    }
}
