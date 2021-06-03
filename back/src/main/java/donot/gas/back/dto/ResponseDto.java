package donot.gas.back.dto;

import donot.gas.back.entity.Order;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
@Builder
public class ResponseDto {
    private final int status;
    private final String responseMessage;
    private final Object data;
}
