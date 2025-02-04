package ksh.example.mybit.trade.service.dto.response;

import ksh.example.mybit.trade.domain.Trade;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TradeHistoryListResponse {

    List<TradeHistoryResponse> histories;

    public TradeHistoryListResponse(List<Trade> trades, Long memberId) {
        this.histories = trades.stream()
                .map(trade -> new TradeHistoryResponse(trade, memberId))
                .toList();
    }
}
