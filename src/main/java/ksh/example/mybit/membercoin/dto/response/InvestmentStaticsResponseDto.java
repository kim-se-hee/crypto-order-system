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

    private double balance;

    private BigDecimal roi;

    public InvestmentStaticsResponseDto(MemberCoin memberCoin, double balance, BigDecimal roi) {
        this.averagePrice = memberCoin.getAveragePrice();
        this.quantity = memberCoin.getQuantity();
        this.balance = balance;
        this.roi = roi;
    }
}
