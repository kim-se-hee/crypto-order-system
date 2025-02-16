package ksh.example.mybit.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ksh.example.mybit.member.dto.request.MemberJoinRequest;
import ksh.example.mybit.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    MemberService memberService;

    @DisplayName("신규 회원을 생성한다")
    @Test
    void memberAdd() throws Exception {
        //given
        MemberJoinRequest request = MemberJoinRequest.builder()
                .name("kim")
                .email("kim@gmail.com")
                .password("123456")
                .build();

        //when //then
        mockMvc.perform(
                post("/member/new")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());

     }

    @DisplayName("신규 회원을 생성할 때 이름은 필수다")
    @Test
    void memberAddWithoutName() throws Exception {
        //given
        MemberJoinRequest request = MemberJoinRequest.builder()
                .email("kim@gmail.com")
                .password("123456")
                .build();

        //when //then
        mockMvc.perform(
                        post("/member/new")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("회원 이름은 필수입니다"));

    }

    @DisplayName("신규 회원을 생성할 때 이메일은 필수다")
    @Test
    void memberAddWithoutEmail() throws Exception {
        //given
        MemberJoinRequest request = MemberJoinRequest.builder()
                .name("kim")
                .password("123456")
                .build();

        //when //then
        mockMvc.perform(
                        post("/member/new")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("이메일은 필수입니다"));

    }

    @DisplayName("신규 회원을 생성할 때 비밀번호는 필수다")
    @Test
    void memberAddWithoutPassword() throws Exception {
        //given
        MemberJoinRequest request = MemberJoinRequest.builder()
                .name("kim")
                .email("kim@gmail.com")
                .build();

        //when //then
        mockMvc.perform(
                        post("/member/new")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("비밀번호는 필수입니다"));

    }

}
