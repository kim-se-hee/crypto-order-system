package ksh.example.mybit.controller.dto;

import ksh.example.mybit.domain.Coin;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

@Getter
@Setter
public class CoinListResponseDto {

    private Integer pageNum;
    private Integer totalPages;
    private boolean hasNext;
    private List<CoinDto> coinList;

    public CoinListResponseDto(Page<Coin> page) {
        this.pageNum = page.getNumber();
        this.totalPages = page.getTotalPages();
        this.hasNext = page.hasNext();
        this.coinList = page.stream()
                .map(CoinDto::new)
                .toList();
    }
}
