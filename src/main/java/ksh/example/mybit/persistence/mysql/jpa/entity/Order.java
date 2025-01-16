package ksh.example.mybit.persistence.mysql.jpa.entity;

import jakarta.persistence.*;
import ksh.example.mybit.persistence.mysql.jpa.entity.dip.OrderCreateValidator;
import ksh.example.mybit.persistence.mysql.jpa.entity.type.OrderSide;
import ksh.example.mybit.persistence.mysql.jpa.entity.type.OrderStatus;
import ksh.example.mybit.persistence.mysql.jpa.entity.type.OrderType;
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

    public Order(OrderCreateValidator validator, Integer amount, OrderSide orderSide, OrderType orderType, BigDecimal limitPrice) {
        validator.validate(amount, orderSide, orderType, limitPrice);
        this.amount = amount;
        this.orderSide = orderSide;
        this.orderType = orderType;
        this.limitPrice = limitPrice;
        this.orderStatus = OrderStatus.PENDING;
    }

    public Order() {

    }

    public void updateAmount(Integer volume) {
        this.amount = this.amount - volume;
    }

    public boolean isFinished() {
        return this.amount == 0;
    }

    public void finish() {
        this.orderStatus = OrderStatus.FINISHED;
    }

    public void cancel() {
        this.orderStatus = OrderStatus.CANCELED;
    }
}
