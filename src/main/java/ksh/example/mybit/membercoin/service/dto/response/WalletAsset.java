package ksh.example.mybit.membercoin.service.dto.response;

import ksh.example.mybit.membercoin.domain.MemberCoin;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class WalletAsset {
    String name;
    String ticker;
    BigDecimal balance;
    BigDecimal quantity;

    public WalletAsset(MemberCoin memberCoin) {
        this.name = memberCoin.getCoin().getName();
        this.ticker = memberCoin.getCoin().getTicker();
        this.balance = memberCoin.getBalance();
        this.quantity = memberCoin.getQuantity();
    }
}
