package donot.gas.back.controller;

import donot.gas.back.auth.JwtTokenProvider;

import donot.gas.back.dto.ErrorDto;
import donot.gas.back.dto.OrderDto;
import donot.gas.back.dto.PurchaseDto;
import donot.gas.back.dto.ResponseDto;
import donot.gas.back.entity.Order;
import donot.gas.back.entity.User;
import donot.gas.back.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import donot.gas.back.exception.user.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;
import static java.util.stream.Collectors.toList;

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

    @GetMapping("/api/purchase")
    public ResponseEntity<?> orderList(HttpServletRequest request) {

        String loginId = jwtTokenProvider.getUserPk(request.getHeader("X-AUTH-TOKEN"));
        User user = orderService.findUser(loginId);
        List<Order> orderList = user.getOrderList();

        List<OrderDto> result = orderList.stream()
                .map(o -> new OrderDto(o))
                .collect(toList());

        ResponseDto responseDto = ResponseDto.builder()
                .status(200)
                .responseMessage("조회 성공")
                .data(result)
                .build();
        return ResponseEntity.ok(responseDto);
    }

    @ExceptionHandler({
            NoExistUserException.class,
    })
    public ResponseEntity<?> handleBadRequest(Exception e) {
        System.err.println(e.getClass());
        ErrorDto error = ErrorDto.builder()
                .status(400)
                .responseMessage(e.getMessage())
                .build();
        return ResponseEntity.badRequest().body(error);
    }

}
