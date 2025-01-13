package ksh.example.mybit.controller.dto;

import ksh.example.mybit.domain.MemberCoin;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@Setter
public class AssetDto {
    String ticker;
    BigDecimal balance;
    Long koreanValue;

    public AssetDto(MemberCoin memberCoin) {
        this.ticker = memberCoin.getCoin().getTicker();
        this.koreanValue = memberCoin.getKoreanWonValue();
        this.balance = new BigDecimal(koreanValue).divide(memberCoin.getCoin().getPrice(), 8, RoundingMode.HALF_UP);
    }
}
