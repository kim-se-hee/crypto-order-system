package ksh.example.mybit.trade.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TradeHistoriesDto {

    List<TradeHistoryDto> histories;

    public TradeHistoriesDto(List<TradeHistoryDto> histories) {
        this.histories = histories;
    }
}
