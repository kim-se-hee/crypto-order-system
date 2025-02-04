package ksh.example.mybit.membercoin.service.dto.response;

import ksh.example.mybit.membercoin.domain.MemberCoin;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WalletAssetListResponse {

    private List<WalletAsset> walletAssets;

    public WalletAssetListResponse(List<MemberCoin> walletAssets) {
        this.walletAssets = walletAssets
                .stream()
                .map(WalletAsset::new)
                .toList();
    }
}
