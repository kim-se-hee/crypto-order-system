package ksh.example.mybit.membercoin.implementation;

import ksh.example.mybit.global.util.BigDecimalCalculateUtil;
import ksh.example.mybit.membercoin.domain.MemberCoin;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
@RequiredArgsConstructor
public class PortfolioAnalyzer {

    public BigDecimal calculateAveragePrice(MemberCoin memberCoin) {

        return BigDecimalCalculateUtil
                .init(memberCoin.getBalance())
                .divide(memberCoin.getQuantity(), 2, RoundingMode.HALF_UP)
                .getValue();
    }

    public BigDecimal calculateTotalValue(MemberCoin memberCoin) {

        return BigDecimalCalculateUtil
                .init(memberCoin.getCoin().getPrice())
                .multiply(memberCoin.getQuantity())
                .getValue();
    }

    public BigDecimal calculateROI(MemberCoin memberCoin, BigDecimal averagePrice) {
        return BigDecimalCalculateUtil
                .init(memberCoin.getCoin().getPrice())
                .subtract(averagePrice)
                .divide(averagePrice, 2, RoundingMode.HALF_UP)
                .multiply(100)
                .getValue();
    }
}
