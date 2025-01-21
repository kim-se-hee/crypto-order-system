package ksh.example.mybit.order.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCreateResponseDto {

    private Long id;

    public OrderCreateResponseDto(Long id) {
        this.id = id;
    }
}
