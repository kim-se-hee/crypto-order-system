package ksh.example.mybit.trade.dto.response;

import ksh.example.mybit.trade.domain.Trade;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TradeHistoryListDto {

    List<TradeHistoryDto> histories;

    public TradeHistoryListDto(List<Trade> trades, Long memberId) {
        this.histories = trades.stream()
                .map(trade -> new TradeHistoryDto(trade, memberId))
                .toList();
    }
}
