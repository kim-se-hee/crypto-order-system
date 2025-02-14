package ksh.example.mybit.order.domain;

import jakarta.persistence.*;
import ksh.example.mybit.coin.domain.Coin;
import ksh.example.mybit.global.util.BigDecimalCalculateUtil;
import ksh.example.mybit.member.domain.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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

    public Order() {

    }

    public void updateVolume(Integer volume) {
        if(volume > this.volume) {
            throw new IllegalArgumentException("남아있는 주문 양이 부족합니다.");
        }

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

    public void setMemberAndCoin(Member member, Coin coin) {
        this.member = member;
        this.coin = coin;
    }

    public BigDecimal getQuantity() {
        return BigDecimalCalculateUtil.init(volume)
                .divide(coin.getPrice(), 8, RoundingMode.HALF_UP)
                .getValue();
    }
}
