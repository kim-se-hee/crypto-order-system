package ksh.example.mybit.persistence.mysql.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;

@Entity
@Getter
public class Coin {
    @Id
    @Column(name = "coin_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String ticker;

    private BigDecimal price;

    public Coin(String ticker, BigDecimal price) {
        this.ticker = ticker;
        this.price = price;
    }

    public Coin() {
    }
}
