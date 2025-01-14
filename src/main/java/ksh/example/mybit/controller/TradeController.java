package ksh.example.mybit.controller;

import ksh.example.mybit.controller.dto.TradeHistoriesDto;
import ksh.example.mybit.controller.dto.TradeHistoryDto;
import ksh.example.mybit.service.TradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TradeController {

    private final TradeService tradeService;

    @GetMapping("trade/history/member/{memberId}/coin/{coinId}")
    public TradeHistoriesDto tradeHistoryOfCoin(@PathVariable Long memberId, @PathVariable Long coinId, Pageable pageable) {
        List<TradeHistoryDto> histories = tradeService.getTradeHistory(memberId, coinId, pageable)
                .stream()
                .map(trade -> new TradeHistoryDto(trade, memberId))
                .toList();

        return new TradeHistoriesDto(histories);
    }

}
