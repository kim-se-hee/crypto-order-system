package ksh.example.mybit.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
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

    public boolean isLessThan(Integer amount){
        return this.koreanWonValue < amount;
    }
}
