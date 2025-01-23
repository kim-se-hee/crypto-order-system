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

    public BigDecimal calculateAveragePrice(MemberCoin memberCoin, BigDecimal executedPrice, BigDecimal executedQuantity) {
        BigDecimal currentValue = BigDecimalCalculateUtil
                .init(memberCoin.getAveragePrice())
                .multiply(memberCoin.getQuantity())
                .getValue();

        BigDecimal newValue = BigDecimalCalculateUtil
                .init(memberCoin.getCoin().getPrice())
                .multiply(executedPrice)
                .getValue();

        BigDecimal totalQuantity = BigDecimalCalculateUtil
                .init(memberCoin.getQuantity())
                .add(executedQuantity)
                .getValue();

        return BigDecimalCalculateUtil
                .init(currentValue)
                .add(newValue)
                .divide(totalQuantity, 4, RoundingMode.HALF_UP)
                .getValue();
    }
}
