package ksh.example.mybit.membercoin.dto.response;

import ksh.example.mybit.membercoin.domain.MemberCoin;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@Setter
public class WalletAssetDto {
    String ticker;
    BigDecimal balance;
    Long koreanValue;

    public WalletAssetDto(MemberCoin memberCoin) {
        this.ticker = memberCoin.getCoin().getTicker();
        this.koreanValue = memberCoin.getBalance();
        this.balance = new BigDecimal(koreanValue).divide(memberCoin.getCoin().getPrice(), 8, RoundingMode.HALF_UP);
    }
}
