package ksh.example.mybit.domain;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Trade {
    @Id
    @Column(name = "trade_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long executedQuantity;
    private BigDecimal executedPrice;
    private Long executedAmount;

    @CreatedDate
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order buyOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order sellOrder;
}
