package ksh.example.mybit.membercoin.domain;

import jakarta.persistence.*;
import ksh.example.mybit.coin.domain.Coin;
import ksh.example.mybit.member.domain.Member;
import lombok.Getter;

import java.math.BigDecimal;

@Entity
@Getter
public class MemberCoin {
    @Id
    @Column(name = "member_coin_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private BigDecimal quantity;

    private Long balance;

    @ManyToOne(fetch = FetchType.LAZY)
    Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    Coin coin;

    public MemberCoin(BigDecimal quantity, Long balance, Member member, Coin coin) {
        this.quantity = quantity;
        this.balance = balance;
        this.member = member;
        this.coin = coin;
    }

    public MemberCoin() {
    }

    public void increaseBalance(Integer amount) {
        this.balance += amount;
    }

    public void decreaseBalance(Integer amount) {
        this.balance -= amount;
    }
}
