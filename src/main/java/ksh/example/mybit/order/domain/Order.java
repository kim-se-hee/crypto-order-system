package ksh.example.mybit.order.domain;

import jakarta.persistence.*;
import ksh.example.mybit.coin.domain.Coin;
import ksh.example.mybit.member.domain.Member;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Order {
    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer volume;
    @Enumerated(EnumType.STRING)
    private OrderSide orderSide;
    @Enumerated(EnumType.STRING)
    private OrderType orderType;
    private BigDecimal limitPrice;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private BigDecimal stopPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Coin coin;

    @CreatedDate
    private LocalDateTime createdAt;

    public Order(Integer volume, OrderSide orderSide, OrderType orderType, BigDecimal limitPrice, Member member, Coin coin) {
        this.volume = volume;
        this.orderSide = orderSide;
        this.orderType = orderType;
        this.limitPrice = limitPrice;
        this.member = member;
        this.coin = coin;
        this.orderStatus = OrderStatus.PENDING;
    }

    public Order() {

    }

    public void updateVolume(Integer volume) {
        this.volume = this.volume - volume;
    }

    public boolean isFinished() {
        return this.volume == 0;
    }

    public void finish() {
        this.orderStatus = OrderStatus.FINISHED;
    }

    public void cancel() {
        this.orderStatus = OrderStatus.CANCELED;
    }
}
