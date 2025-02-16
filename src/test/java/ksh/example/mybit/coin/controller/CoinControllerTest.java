package ksh.example.mybit.coin.controller;

import ksh.example.mybit.coin.service.CoinService;
import ksh.example.mybit.coin.service.dto.response.CoinListResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CoinController.class)
class CoinControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CoinService coinService;

    @DisplayName("상장 코인의 시세 정보를 조회한다")
    @Test
    void marketList() throws Exception {
        //given
        CoinListResponse response = new CoinListResponse(Page.empty());
        BDDMockito.given(coinService.getListedCoins(any(PageRequest.class)))
                .willReturn(response);

        //when //then
        mockMvc.perform(
                        get("/coins")
                                .param("page", "0")
                                .param("size", "10")
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pageNum").isNumber())
                .andExpect(jsonPath("$.totalPages").isNumber())
                .andExpect(jsonPath("$.hasNext").isBoolean())
                .andExpect(jsonPath("$.coinList").isArray());

    }
}
