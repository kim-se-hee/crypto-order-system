package ksh.example.mybit.implementation;

import ksh.example.mybit.domain.MemberCoin;
import ksh.example.mybit.repository.MemberCoinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WalletReader {

    private final MemberCoinRepository memberCoinRepository;

    public MemberCoin readMemberCoin(Long memberId, Long coinId) {
        return memberCoinRepository.findByMemberIdAndCoinId(memberId, coinId)
                .orElseThrow(() -> new IllegalArgumentException("해당 자산을 보유하고 있지 않습니다"));
    }
}
