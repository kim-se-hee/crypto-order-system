package ksh.example.mybit.order.service.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCreateResponse {

    private Long id;

    public OrderCreateResponse(Long id) {
        this.id = id;
    }
}
