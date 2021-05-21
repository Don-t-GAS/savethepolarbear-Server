package donot.gas.back.controller;

import donot.gas.back.auth.JwtTokenProvider;
import donot.gas.back.dto.PurchaseDto;
import donot.gas.back.dto.ResponseDto;
import donot.gas.back.entity.User;
import donot.gas.back.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@RestController
@RequiredArgsConstructor
public class PurchaseController {
    private final JwtTokenProvider jwtTokenProvider;
    private final OrderService orderService;

    @PostMapping("/api/purchase")
    public ResponseEntity<?> purchase(@RequestBody HashMap<String, String> product, HttpServletRequest request) {

        String company = product.get("company");
        String kinds = product.get("kinds");
        String model = product.get("model");
        Integer grade = Integer.parseInt(product.get("grade"));

        String loginId = jwtTokenProvider.getUserPk(request.getHeader("X-AUTH-TOKEN"));
        User user = orderService.findUser(loginId);

        orderService.createOrder(company, kinds, model, grade, user);

        ResponseDto responseDto = ResponseDto.builder()
                .status(200)
                .responseMessage("구매 성공")
                .data(new PurchaseDto(user.getPoint(), user.getRank(), user.getDiscount()))
                .build();
        return ResponseEntity.ok(responseDto);
    }


}
