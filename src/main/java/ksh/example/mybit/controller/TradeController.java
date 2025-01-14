package ksh.example.mybit.controller;

import ksh.example.mybit.controller.dto.TradeHistoriesDto;
import ksh.example.mybit.controller.dto.TradeHistoryDto;
import ksh.example.mybit.service.MatchingService;
import ksh.example.mybit.service.TradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

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
