package ksh.example.mybit.membercoin.controller;

import jakarta.validation.Valid;
import ksh.example.mybit.membercoin.dto.request.DepositWithdrawForm;
import ksh.example.mybit.membercoin.dto.request.InvestmentStaticsRequestDto;
import ksh.example.mybit.membercoin.dto.response.InvestmentStaticsResponseDto;
import ksh.example.mybit.membercoin.dto.response.WalletAssetListResponseDto;
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
    public ResponseEntity<WalletAssetListResponseDto> viewWallet(@PathVariable Long id) {
        WalletAssetListResponseDto allCoinsInWallet = memberCoinService.findAllCoinsInWallet(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(allCoinsInWallet);
    }

    @GetMapping("/static")
    public ResponseEntity<InvestmentStaticsResponseDto> investmentStatics(@Valid @RequestBody InvestmentStaticsRequestDto requestDto) {
        InvestmentStaticsResponseDto investmentStatic = memberCoinService.getInvestmentStatic(requestDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(investmentStatic);
    }

    @PostMapping("/deposit")
    public ResponseEntity<Boolean> deposit(@Valid @ModelAttribute DepositWithdrawForm form) {
        memberCoinService.deposit(form.getMemberId(), form.getCoinId(), form.getVolume());

        return ResponseEntity.ok(true);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Boolean> withdraw(@Valid @ModelAttribute DepositWithdrawForm form) {
        memberCoinService.withdraw(form.getMemberId(), form.getCoinId(), form.getVolume());

        return ResponseEntity.ok(true);
    }
}
