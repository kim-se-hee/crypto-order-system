package ksh.example.mybit.controller;

import ksh.example.mybit.domain.OrderSide;
import ksh.example.mybit.domain.OrderType;
import ksh.example.mybit.service.CoinService;
import ksh.example.mybit.service.MemberService;
import ksh.example.mybit.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;
    @MockBean
    private MemberService memberService;
    @MockBean
    private CoinService coinService;

    @Test
    public void 정상_url() throws Exception {
        mockMvc.perform(post("/order")
                        .param("memberId", "1")
                        .param("coinId", "1")
                        .param("orderAmount", "10000")
                        .param("orderSide", OrderSide.SELL.name())
                        .param("orderType", OrderType.LIMIT.name())
                        .param("limit", "10000"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @ParameterizedTest
    @MethodSource("provideURLs")
    public void 요청_파라미터가_잘못된_url들(Map<String, String> params) throws Exception {
        MockHttpServletRequestBuilder request = post("/order");
        params.forEach(request::param);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    private static Stream<Map<String, String>> provideURLs() {
        return Stream.of(
                makeMessageBody(null, "1L", "10000", "SELL", "LIMIT", "10000"),
                makeMessageBody("1L", null, "10000", "SELL", "LIMIT", "10000"),
                makeMessageBody("1L", "1L", "0", "SELL", "LIMIT", "10000"),
                makeMessageBody("1L", "1L", "200000000", "SELL", "LIMIT", "10000"),
                makeMessageBody("1L", "1L", "20000", null, "LIMIT", "10000"),
                makeMessageBody("1L", "1L", "20000", "BUY", null, "10000")
        );
    }

    private static Map<String, String> makeMessageBody(
            String memberId,
            String coinId,
            String orderAmount,
            String orderSide,
            String orderType,
            String limitPrice) {

        Map<String, String> map = new HashMap<>();
        if (memberId != null) {
            map.put("memberId", memberId);
        }

        if (coinId != null) {
            map.put("coinId", coinId);
        }

        if (orderAmount != null) {
            map.put("orderAmount", orderAmount);
        }

        if (orderSide != null) {
            map.put("orderSide", orderSide);
        }

        if (orderType != null) {
            map.put("orderType", orderType);
        }

        if (limitPrice != null) {
            map.put("limitPrice", limitPrice);
        }

        return map;
    }
}
