package ksh.example.mybit.member.service.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberJoinResponse {

    public Long id;

    public MemberJoinResponse(Long id) {
        this.id = id;
    }

}
