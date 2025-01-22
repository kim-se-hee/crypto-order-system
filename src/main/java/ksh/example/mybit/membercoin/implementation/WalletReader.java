package ksh.example.mybit.membercoin.implementation;

import ksh.example.mybit.membercoin.domain.MemberCoin;
import ksh.example.mybit.membercoin.repository.MemberCoinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WalletReader {

    private final MemberCoinRepository memberCoinRepository;

    public MemberCoin readMemberCoinWithLock(Long memberId, Long coinId) {
        return memberCoinRepository.findWithWriteLockByMemberIdAndCoinId(memberId, coinId)
                .orElseThrow(() -> new IllegalArgumentException("해당 자산을 보유하고 있지 않습니다"));
    }

    public List<MemberCoin> readAllCoinOfMember(Long memberId) {
        return memberCoinRepository.findByMemberId(memberId);
    }

    public MemberCoin readByMemberIdAndCoinId(Long memberId, Long coinId) {
        return memberCoinRepository.findWithWriteLockByMemberIdAndCoinId(memberId, coinId)
                .orElseThrow(() -> new IllegalArgumentException("해당 자산을 보유하고 있지 않습니다"));
    }
}
