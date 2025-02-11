package ksh.example.mybit.membercoin.service.dto.response;

import ksh.example.mybit.membercoin.domain.MemberCoin;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class InvestmentStaticsResponse {

    private BigDecimal averagePrice;

    private BigDecimal quantity;

    private double balance;

    private double roi;

    public InvestmentStaticsResponse(MemberCoin memberCoin, double balance, double roi) {
        this.averagePrice = memberCoin.getAveragePrice();
        this.quantity = memberCoin.getQuantity();
        this.balance = balance;
        this.roi = roi;
    }
}
