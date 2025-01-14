package ksh.example.mybit.controller.dto;

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
