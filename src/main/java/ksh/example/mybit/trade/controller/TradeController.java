package ksh.example.mybit.trade.controller;

import ksh.example.mybit.trade.service.dto.response.TradeHistoryListResponse;
import ksh.example.mybit.trade.service.TradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TradeController {

    private final TradeService tradeService;

    @GetMapping("trade/history")
    public ResponseEntity<TradeHistoryListResponse> tradeHistoryOfCoin(@RequestParam Long memberId, @RequestParam Long coinId, Pageable pageable) {
        TradeHistoryListResponse tradeHistoryList = tradeService.getTradeHistory(memberId, coinId, pageable);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(tradeHistoryList);
    }

}
