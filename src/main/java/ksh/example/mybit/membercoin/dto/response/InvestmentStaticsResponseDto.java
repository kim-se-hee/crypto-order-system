package ksh.example.mybit.membercoin.dto.response;

import ksh.example.mybit.membercoin.domain.MemberCoin;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class InvestmentStaticsResponseDto {

    private BigDecimal averagePrice;

    private BigDecimal quantity;

    private Long balance;

    private BigDecimal roi;

    public InvestmentStaticsResponseDto(MemberCoin memberCoin, BigDecimal averagePrice, BigDecimal roi) {
        this.averagePrice = averagePrice;
        this.quantity = memberCoin.getQuantity();
        this.balance = memberCoin.getBalance();
        this.roi = roi;
    }
}
