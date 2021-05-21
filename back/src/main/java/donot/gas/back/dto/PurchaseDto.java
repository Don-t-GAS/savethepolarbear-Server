package donot.gas.back.dto;

import donot.gas.back.entity.Rank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PurchaseDto {
    private final Long userPoint;
    private final Rank userRank;
    private final Integer userDiscount;
}
