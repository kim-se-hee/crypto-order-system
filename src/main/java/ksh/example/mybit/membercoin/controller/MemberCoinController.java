package ksh.example.mybit.membercoin.controller;

import jakarta.validation.Valid;
import ksh.example.mybit.membercoin.dto.request.FundTransferRequest;
import ksh.example.mybit.membercoin.dto.request.InvestmentStaticsRequest;
import ksh.example.mybit.membercoin.service.dto.response.InvestmentStaticsResponse;
import ksh.example.mybit.membercoin.service.dto.response.WalletAssetListResponse;
import ksh.example.mybit.membercoin.service.MemberCoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberCoinController {

    private final MemberCoinService memberCoinService;

    @GetMapping("/wallet/{id}")
    public ResponseEntity<WalletAssetListResponse> viewWallet(@PathVariable("id") Long id) {
        WalletAssetListResponse allCoinsInWallet = memberCoinService.findAllCoinsInWallet(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(allCoinsInWallet);
    }

    @GetMapping("/static")
    public ResponseEntity<InvestmentStaticsResponse> investmentStatics(@Valid @ModelAttribute InvestmentStaticsRequest requestDto) {
        InvestmentStaticsResponse investmentStatic = memberCoinService.getInvestmentStatic(requestDto.toServiceRequest());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(investmentStatic);
    }

    @PostMapping("/deposit")
    public ResponseEntity<Boolean> deposit(@Valid @RequestBody FundTransferRequest request) {
        memberCoinService.deposit(request.toServiceRequest());

        return ResponseEntity.ok(true);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Boolean> withdraw(@Valid @RequestBody FundTransferRequest request) {
        memberCoinService.withdraw(request.toServiceRequest());

        return ResponseEntity.ok(true);
    }
}
