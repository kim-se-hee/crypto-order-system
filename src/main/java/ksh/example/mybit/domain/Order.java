package ksh.example.mybit.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer amount;
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

    public Order(Integer amount, OrderSide orderSide, OrderType orderType, BigDecimal limitPrice, Member member, Coin coin){
        this.amount =  amount;
        this.orderSide = orderSide;
        this.orderType = orderType;
        this.limitPrice = limitPrice;
        this.member = member;
        this.coin = coin;
    }

    public Order() {

    }
}
