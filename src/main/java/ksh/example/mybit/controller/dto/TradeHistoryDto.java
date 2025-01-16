package ksh.example.mybit.controller.dto;

import ksh.example.mybit.domain.OrderSide;
import ksh.example.mybit.domain.Trade;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class TradeHistoryDto {

    OrderSide orderSide;
    String ticker;
    LocalDateTime tradeDateTime;
    BigDecimal executedQuantity;
    Integer executedVolume;

    public TradeHistoryDto(Trade trade, Long memberId) {
        this.orderSide = trade.getSellOrder().getMember().getId() == memberId ? OrderSide.SELL : OrderSide.BUY;
        this.ticker = trade.getSellOrder().getCoin().getTicker();
        this.tradeDateTime = trade.getCreatedAt();
        this.executedQuantity = trade.getExecutedQuantity();
        this.executedVolume = trade.getExecutedVolume();
    }
}
