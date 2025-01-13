package ksh.example.mybit.controller;

import jakarta.validation.Valid;
import ksh.example.mybit.controller.dto.AssetDto;
import ksh.example.mybit.controller.form.DepositWithdrawForm;
import ksh.example.mybit.service.MemberCoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberCoinController {

    private final MemberCoinService memberCoinService;

    @GetMapping("/wallet/{id}")
    public List<AssetDto> viewWallet(@PathVariable Long id) {
        return memberCoinService.findAllCoinsInWallet(id)
                .stream()
                .map(AssetDto::new)
                .toList();
    }

    @PostMapping("/deposit")
    public ResponseEntity<Boolean> deposit(@Valid @ModelAttribute DepositWithdrawForm form) {
        memberCoinService.deposit(form.getMemberId(), form.getCoinId(), form.getAmount());

        return ResponseEntity.ok(true);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Boolean> withdraw(@Valid @ModelAttribute DepositWithdrawForm form) {
        memberCoinService.withdraw(form.getMemberId(), form.getCoinId(), form.getAmount());

        return ResponseEntity.ok(true);
    }
}
