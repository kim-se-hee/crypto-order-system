package ksh.example.mybit.coin.service.dto.response;

import ksh.example.mybit.coin.domain.Coin;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
public class CoinListResponse {

    private Integer pageNum;
    private Integer totalPages;
    private boolean hasNext;
    private List<CoinResponse> coinList;

    public CoinListResponse(Page<Coin> page) {
        this.pageNum = page.getNumber();
        this.totalPages = page.getTotalPages();
        this.hasNext = page.hasNext();
        this.coinList = page.stream()
                .map(CoinResponse::new)
                .toList();
    }
}
