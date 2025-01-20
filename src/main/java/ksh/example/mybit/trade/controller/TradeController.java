package ksh.example.mybit.trade.controller;

import ksh.example.mybit.trade.dto.response.TradeHistoriesDto;
import ksh.example.mybit.trade.dto.response.TradeHistoryDto;
import ksh.example.mybit.trade.service.TradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TradeController {

    private final TradeService tradeService;

    @GetMapping("trade/history")
    public TradeHistoriesDto tradeHistoryOfCoin(@RequestParam Long memberId, @RequestParam Long coinId, Pageable pageable) {
        List<TradeHistoryDto> histories = tradeService.getTradeHistory(memberId, coinId, pageable)
                .stream()
                .map(trade -> new TradeHistoryDto(trade, memberId))
                .toList();

        return new TradeHistoriesDto(histories);
    }

}
