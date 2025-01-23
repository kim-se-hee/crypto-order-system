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

    private Long balance;

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
        this.quantity = this.quantity.subtract(quantity);
    }
}
