package ksh.example.mybit.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
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
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Coin coin;

    @CreatedDate
    private LocalDateTime createdAt;

    public Order(Integer amount, OrderSide orderSide, OrderType orderType, BigDecimal limitPrice, Member member, Coin coin){
        this.amount =  amount;
        this.orderSide = orderSide;
        this.orderType = orderType;
        this.limitPrice = limitPrice;
        this.member = member;
        this.coin = coin;
        this.orderStatus = OrderStatus.PENDING;
    }

    public Order() {

    }

    public void updateAmount(Integer volume){
        this.amount = this.amount - volume;
    }

    public boolean isFinished(){
        return this.amount == 0;
    }

    public void finish() {
        this.orderStatus = OrderStatus.FINISHED;
    }
}
