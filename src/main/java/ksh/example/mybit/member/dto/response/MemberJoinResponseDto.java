package ksh.example.mybit.member.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberJoinResponseDto {

    public Long id;

    public MemberJoinResponseDto(Long id) {
        this.id = id;
    }

}
