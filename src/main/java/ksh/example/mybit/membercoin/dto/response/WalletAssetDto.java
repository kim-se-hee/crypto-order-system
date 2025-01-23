package ksh.example.mybit.membercoin.dto.response;

import ksh.example.mybit.membercoin.domain.MemberCoin;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@Setter
public class WalletAssetDto {
    String name;
    String ticker;
    BigDecimal balance;
    BigDecimal quantity;

    public WalletAssetDto(MemberCoin memberCoin) {
        this.name = memberCoin.getCoin().getName();
        this.ticker = memberCoin.getCoin().getTicker();
        this.balance = memberCoin.getCoin().getPrice().multiply(memberCoin.getQuantity());
        this.quantity = memberCoin.getQuantity();
    }
}
