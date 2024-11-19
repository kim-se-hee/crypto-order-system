package ksh.example.mybit.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class Order {
    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long quantity;
    private Long amount;
    @Enumerated(EnumType.STRING)
    private OrderSide orderSide;
    @Enumerated(EnumType.STRING)
    private OrderType orderType;
    private BigDecimal limitPrice;
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Coin coin;
}
