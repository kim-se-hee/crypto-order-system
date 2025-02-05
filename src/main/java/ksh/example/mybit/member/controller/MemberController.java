package ksh.example.mybit.member.controller;

import jakarta.validation.Valid;
import ksh.example.mybit.member.dto.request.MemberJoinRequest;
import ksh.example.mybit.member.service.dto.response.MemberJoinResponse;
import ksh.example.mybit.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/member/new")
    public ResponseEntity<MemberJoinResponse> memberAdd(@Valid @RequestBody MemberJoinRequest reqeustDto) {
        MemberJoinResponse responseDto = memberService.join(reqeustDto.toServiceRequest());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDto);
    }
}
