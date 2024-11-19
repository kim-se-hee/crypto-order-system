package ksh.example.mybit.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class Coin {
    @Id
    @Column(name = "coin_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String ticker;

    private BigDecimal price;
}
