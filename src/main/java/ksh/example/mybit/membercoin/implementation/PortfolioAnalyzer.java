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

    public double calculateBalance(MemberCoin memberCoin) {

        return BigDecimalCalculateUtil
                .init(memberCoin.getCoin().getPrice())
                .multiply(memberCoin.getQuantity())
                .getValue()
                .doubleValue();
    }

    public BigDecimal calculateROI(MemberCoin memberCoin) {
        return BigDecimalCalculateUtil
                .init(memberCoin.getCoin().getPrice())
                .subtract(memberCoin.getAveragePrice())
                .divide(memberCoin.getAveragePrice(), 2, RoundingMode.HALF_UP)
                .multiply(100)
                .getValue();
    }
}
