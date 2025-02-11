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

    public static double calculateBalance(MemberCoin memberCoin) {

        return BigDecimalCalculateUtil
                .init(memberCoin.getCoin().getPrice())
                .multiply(memberCoin.getQuantity())
                .getValue()
                .doubleValue();
    }

    public static double calculateROI(MemberCoin memberCoin) {
        return BigDecimalCalculateUtil
                .init(memberCoin.getCoin().getPrice())
                .subtract(memberCoin.getAveragePrice())
                .multiply(100)
                .divide(memberCoin.getAveragePrice(), 2, RoundingMode.HALF_UP)
                .getValue()
                .doubleValue();
    }


    public static BigDecimal calculateAveragePrice(MemberCoin memberCoin, BigDecimal executedPrice, BigDecimal executedQuantity) {
        BigDecimal currentValue = BigDecimalCalculateUtil
                .init(memberCoin.getAveragePrice())
                .multiply(memberCoin.getQuantity())
                .getValue();

        BigDecimal newValue = BigDecimalCalculateUtil
                .init(executedPrice)
                .multiply(executedQuantity)
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
