package ksh.example.mybit.member.controller;

import jakarta.validation.Valid;
import ksh.example.mybit.member.dto.request.MemberJoinReqeustDto;
import ksh.example.mybit.member.dto.response.MemberJoinResponseDto;
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
    public ResponseEntity<MemberJoinResponseDto> memberAdd(@Valid @RequestBody MemberJoinReqeustDto reqeustDto) {
        MemberJoinResponseDto responseDto = memberService.join(reqeustDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDto);
    }
}
