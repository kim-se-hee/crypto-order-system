package ksh.example.mybit.trade.service.dto.response;

import ksh.example.mybit.order.domain.OrderSide;
import ksh.example.mybit.trade.domain.Trade;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class TradeHistoryResponse {

    OrderSide orderSide;
    String ticker;
    LocalDateTime tradeDateTime;
    BigDecimal executedQuantity;
    Integer executedVolume;

    public TradeHistoryResponse(Trade trade, Long memberId) {
        this.orderSide = trade.getTradeOrderSideFor(memberId);
        this.ticker = trade.getSellOrder().getCoin().getTicker();
        this.tradeDateTime = trade.getCreatedAt();
        this.executedQuantity = trade.getExecutedQuantity();
        this.executedVolume = trade.getExecutedVolume();
    }
}
