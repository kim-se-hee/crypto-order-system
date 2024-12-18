package ksh.example.mybit.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Trade {
    @Id
    @Column(name = "trade_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long executedQuantity;
    private BigDecimal executedPrice;
    private Integer executedAmount;

    @CreatedDate
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order buyOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order sellOrder;

    public Trade(BigDecimal executedPrice, Integer executedAmount, Order order, Order matchingOrder) {
        this.executedPrice = executedPrice;
        this.executedAmount = executedAmount;
        this.buyOrder = order.getOrderSide() == OrderSide.BUY ? order : matchingOrder;
        this.sellOrder = order.getOrderSide() == OrderSide.SELL ? order : matchingOrder;
    }

    public Trade() {
    }
    
}
