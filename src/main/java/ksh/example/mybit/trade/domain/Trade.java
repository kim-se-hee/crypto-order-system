package ksh.example.mybit.trade.domain;

import jakarta.persistence.*;
import ksh.example.mybit.order.domain.Order;
import ksh.example.mybit.order.domain.OrderSide;
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
    private BigDecimal executedQuantity;
    private BigDecimal executedPrice;
    private Integer executedVolume;

    @CreatedDate
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order buyOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order sellOrder;

    public Trade(BigDecimal executedQuantity, BigDecimal executedPrice, Integer executedVolume, Order order, Order matchingOrder) {
        this.executedQuantity = executedQuantity;
        this.executedPrice = executedPrice;
        this.executedVolume = executedVolume;
        this.buyOrder = order.getOrderSide() == OrderSide.BUY ? order : matchingOrder;
        this.sellOrder = order.getOrderSide() == OrderSide.SELL ? order : matchingOrder;
    }

    public Trade() {
    }

}
