package ksh.example.mybit.member.controller;

import jakarta.validation.Valid;
import ksh.example.mybit.member.domain.Member;
import ksh.example.mybit.member.dto.request.MemberJoinForm;
import ksh.example.mybit.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/member/new")
    public ResponseEntity<Map<String, String>> memberAdd(@Valid @ModelAttribute MemberJoinForm form) {
        String email = form.getEmail();
        String password = form.getPassword();
        String name = form.getName();
        Member member = memberService.join(email, password, name);

        Map<String, String> responseBody = Map.of(
                "id", String.valueOf(member.getId())
        );

        return ResponseEntity.ok(responseBody);
    }
}
