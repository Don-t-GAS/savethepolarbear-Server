package donot.gas.back.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnswerDto {
    private final boolean isAnswer;
    private final Integer upPoint;
}
