package ksh.example.mybit.controller;

import jakarta.validation.Valid;
import ksh.example.mybit.controller.form.DepositWithdrawForm;
import ksh.example.mybit.service.MemberCoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberCoinController {

    private final MemberCoinService memberCoinService;

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
