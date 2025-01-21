package ksh.example.mybit.membercoin.dto.response;

import ksh.example.mybit.membercoin.domain.MemberCoin;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WalletAssetListResponseDto {

    private List<WalletAssetDto> walletAssets;

    public WalletAssetListResponseDto(List<MemberCoin> walletAssets) {
        this.walletAssets = walletAssets
                .stream()
                .map(WalletAssetDto::new)
                .toList();
    }
}
