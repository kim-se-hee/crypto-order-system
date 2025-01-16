package ksh.example.mybit.persistence.mysql.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;

@Entity
@Getter
public class MemberCoin {
    @Id
    @Column(name = "member_coin_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private BigDecimal balance;

    private Long koreanWonValue;

    @ManyToOne(fetch = FetchType.LAZY)
    Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    Coin coin;

    public MemberCoin(BigDecimal balance, Long koreanWonValue, Member member, Coin coin) {
        this.balance = balance;
        this.koreanWonValue = koreanWonValue;
        this.member = member;
        this.coin = coin;
    }

    public MemberCoin() {
    }

    public void increaseKoreanWonValue(Integer amount) {
        this.koreanWonValue += amount;
    }

    public void decreaseKoreanWonValue(Integer amount) {
        this.koreanWonValue -= amount;
    }
}
