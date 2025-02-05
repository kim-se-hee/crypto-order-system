package ksh.example.mybit.membercoin.domain;

import jakarta.persistence.*;
import ksh.example.mybit.coin.domain.Coin;
import ksh.example.mybit.member.domain.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberCoin {
    @Id
    @Column(name = "member_coin_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private BigDecimal quantity;

    private BigDecimal averagePrice;

    @ManyToOne(fetch = FetchType.LAZY)
    Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    Coin coin;

    public MemberCoin() {
    }

    public void increaseQuantity(BigDecimal quantity) {
        this.quantity = this.quantity.add(quantity);
    }

    public void decreaseQuantity(BigDecimal quantity) {
        if(this.quantity.compareTo(quantity) < 0) {
            throw new IllegalArgumentException("차감할 보유 수량이 부족합니다.");
        }

        this.quantity = this.quantity.subtract(quantity);
    }

    public void updateAveragePrice(BigDecimal averagePrice) {
        if(BigDecimal.ZERO.compareTo(averagePrice) > 0) {
            throw new IllegalArgumentException("평단가는 음수가 될 수 없습니다.");
        }
        this.averagePrice = averagePrice;
    }
}
