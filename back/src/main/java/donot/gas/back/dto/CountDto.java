package donot.gas.back.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public class CountDto {
    private final Long fisrtCnt;
    private final Long secondCnt;
    private final Long thirdCnt;
    private final Long fourthCnt;
    private final Long fivethCnt;
}
