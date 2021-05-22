package donot.gas.back.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public class CountDto {
    private final Integer fisrtCnt;
    private final Integer secondCnt;
    private final Integer thirdCnt;
    private final Integer fourthCnt;
    private final Integer fivethCnt;
}
