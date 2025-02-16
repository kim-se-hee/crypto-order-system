package ksh.example.mybit.membercoin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ksh.example.mybit.coin.domain.Coin;
import ksh.example.mybit.member.domain.Member;
import ksh.example.mybit.membercoin.domain.MemberCoin;
import ksh.example.mybit.membercoin.dto.request.FundTransferRequest;
import ksh.example.mybit.membercoin.dto.request.InvestmentStaticsRequest;
import ksh.example.mybit.membercoin.service.MemberCoinService;
import ksh.example.mybit.membercoin.service.dto.response.WalletAssetListResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MemberCoinController.class)
class MemberCoinControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberCoinService memberCoinService;


    @DisplayName("회원이 보유한 모든 자산을 조회한다")
    @Test
    void viewWallet() throws Exception {
        //given
        Coin coin = Coin.builder()
                .name("Bitcoin")
                .ticker("BTC")
                .price(new BigDecimal("50000")) // 예시 가격
                .build();

        Member member = Member.builder()
                .id(1L)
                .build();

        MemberCoin memberCoin = MemberCoin.builder()
                .id(1L)
                .quantity(new BigDecimal("2"))
                .averagePrice(new BigDecimal("50000"))
                .coin(coin)
                .member(member)
                .build();

        given(memberCoinService.findAllCoinsInWallet(anyLong()))
                        .willReturn(new WalletAssetListResponse(List.of(memberCoin)));

        //when //then
        mockMvc.perform(
                get("/wallet/{id}", 1l)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletAssets").isArray())
                .andExpect(jsonPath("$.walletAssets[0].name").isString())
                .andExpect(jsonPath("$.walletAssets[0].ticker").isString())
                .andExpect(jsonPath("$.walletAssets[0].balance").isNumber())
                .andExpect(jsonPath("$.walletAssets[0].quantity").isNumber());

     }

    @DisplayName("자산 수익 현황을 조회 시 회원 id는 필수이다")
    @Test
    void investmentStaticsWithoutMemberId() throws Exception {
        //when //then
        mockMvc.perform(
                        get("/static")
                                .param("coinId", "1")
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("회원 id는 필수입니다"));

    }

    @DisplayName("자산 수익 현황을 조회 시 회원 id는 양의 정수이다")
    @Test
    void investmentStaticsNonPositiveMemberId() throws Exception {
        //given
        InvestmentStaticsRequest request = InvestmentStaticsRequest.builder()
                .memberId(0l)
                .coinId(1L)
                .build();

        //when //then
        mockMvc.perform(
                        get("/static")
                                .param("memberId", "0")
                                .param("coinId", "1")
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("회원 id는 양의 정수입니다"));

    }

    @DisplayName("자산 수익 현황을 조회 시 회원 id는 필수이다")
    @Test
    void investmentStaticsWithoutCoinId() throws Exception {
        //given
        InvestmentStaticsRequest request = InvestmentStaticsRequest.builder()
                .memberId(1L)
                .build();

        //when //then
        mockMvc.perform(
                        get("/static")
                                .param("memberId", "1")
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("코인 id는 필수입니다"));

    }

    @DisplayName("자산 수익 현황을 조회 시 회원 id는 양의 정수이다")
    @Test
    void investmentStaticsNonPositiveCoinId() throws Exception {
        //given
        InvestmentStaticsRequest request = InvestmentStaticsRequest.builder()
                .memberId(1l)
                .coinId(0L)
                .build();

        //when //then
        mockMvc.perform(
                        get("/static")
                                .param("memberId", "1")
                                .param("coinId", "0")
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("코인 id는 양의 정수입니다"));

    }

    @DisplayName("입금 시 회원 id는 필수이다")
    @Test
    void depositWithoutMemberId() throws Exception {
        //given
        FundTransferRequest request = FundTransferRequest.builder()
                .coinId(1l)
                .quantity(new BigDecimal("2"))
                .build();

        //when //then
        mockMvc.perform(
                post("/deposit")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("회원 id는 필수입니다"));

     }

    @DisplayName("입금 시 회원 id는 양수이다")
    @Test
    void depositWithNonPositiveMemberId() throws Exception {
        //given
        FundTransferRequest request = FundTransferRequest.builder()
                .memberId(0l)
                .coinId(1l)
                .quantity(new BigDecimal("2"))
                .build();

        //when //then
        mockMvc.perform(
                        post("/deposit")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("회원 id는 양의 정수입니다"));

    }

    @DisplayName("입금 시 코인 id는 필수이다")
    @Test
    void depositWithoutCoinId() throws Exception {
        //given
        FundTransferRequest request = FundTransferRequest.builder()
                .memberId(1l)
                .quantity(new BigDecimal("2"))
                .build();

        //when //then
        mockMvc.perform(
                        post("/deposit")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("코인 id는 필수입니다"));

    }

    @DisplayName("입금 시 회원 id는 양수이다")
    @Test
    void depositWithNonPositiveCoinId() throws Exception {
        //given
        FundTransferRequest request = FundTransferRequest.builder()
                .memberId(1l)
                .coinId(0l)
                .quantity(new BigDecimal("2"))
                .build();

        //when //then
        mockMvc.perform(
                        post("/deposit")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("코인 id는 양의 정수입니다"));

    }

    @DisplayName("입금 시 수량은 필수이다")
    @Test
    void depositWithoutQuantity() throws Exception {
        //given
        FundTransferRequest request = FundTransferRequest.builder()
                .memberId(1l)
                .coinId(1l)
                .build();

        //when //then
        mockMvc.perform(
                        post("/deposit")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("수량은 필수입니다"));

    }

    @DisplayName("입금 시 수량은 양수이다")
    @Test
    void depositWithNonPositiveQuantity() throws Exception {
        //given
        FundTransferRequest request = FundTransferRequest.builder()
                .memberId(1l)
                .coinId(1l)
                .quantity(new BigDecimal("0"))
                .build();

        //when //then
        mockMvc.perform(
                        post("/deposit")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("수량은 양수입니다"));

    }

    @DisplayName("출금 시 회원 id는 필수이다")
    @Test
    void withdrawWithoutMemberId() throws Exception {
        //given
        FundTransferRequest request = FundTransferRequest.builder()
                .coinId(1l)
                .quantity(new BigDecimal("2"))
                .build();

        //when //then
        mockMvc.perform(
                        post("/withdraw")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("회원 id는 필수입니다"));

    }

    @DisplayName("출금 시 회원 id는 양수이다")
    @Test
    void withdrawWithNonPositiveMemberId() throws Exception {
        //given
        FundTransferRequest request = FundTransferRequest.builder()
                .memberId(0l)
                .coinId(1l)
                .quantity(new BigDecimal("2"))
                .build();

        //when //then
        mockMvc.perform(
                        post("/withdraw")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("회원 id는 양의 정수입니다"));

    }

    @DisplayName("출금 시 코인 id는 필수이다")
    @Test
    void withdrawWithoutCoinId() throws Exception {
        //given
        FundTransferRequest request = FundTransferRequest.builder()
                .memberId(1l)
                .quantity(new BigDecimal("2"))
                .build();

        //when //then
        mockMvc.perform(
                        post("/withdraw")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("코인 id는 필수입니다"));

    }

    @DisplayName("출금 시 회원 id는 양수이다")
    @Test
    void withdrawWithNonPositiveCoinId() throws Exception {
        //given
        FundTransferRequest request = FundTransferRequest.builder()
                .memberId(1l)
                .coinId(0l)
                .quantity(new BigDecimal("2"))
                .build();

        //when //then
        mockMvc.perform(
                        post("/withdraw")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("코인 id는 양의 정수입니다"));

    }

    @DisplayName("출금 시 수량은 필수이다")
    @Test
    void withdrawWithoutQuantity() throws Exception {
        //given
        FundTransferRequest request = FundTransferRequest.builder()
                .memberId(1l)
                .coinId(1l)
                .build();

        //when //then
        mockMvc.perform(
                        post("/withdraw")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("수량은 필수입니다"));

    }

    @DisplayName("출금 시 수량은 양수이다")
    @Test
    void withdrawWithNonPositiveQuantity() throws Exception {
        //given
        FundTransferRequest request = FundTransferRequest.builder()
                .memberId(1l)
                .coinId(1l)
                .quantity(new BigDecimal("0"))
                .build();

        //when //then
        mockMvc.perform(
                        post("/withdraw")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("수량은 양수입니다"));

    }

}
