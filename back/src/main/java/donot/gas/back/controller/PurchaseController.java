package donot.gas.back.controller;

import donot.gas.back.auth.JwtTokenProvider;
import donot.gas.back.dto.PointDto;
import donot.gas.back.dto.PurchaseDto;
import donot.gas.back.dto.ResponseDto;
import donot.gas.back.entity.Rank;
import donot.gas.back.entity.User;
import donot.gas.back.exception.user.NoExistUserException;
import donot.gas.back.repository.UserRepository;
import donot.gas.back.service.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Optional;

@RestController
public class PurchaseController {
    private final JwtTokenProvider jwtTokenProvider;
    private final PointService pointService;

    @Autowired
    public PurchaseController(JwtTokenProvider jwtTokenProvider, PointService pointService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.pointService = pointService;
    }

    @PostMapping("/api/purchase")
    public ResponseEntity<?> purchase(@RequestBody HashMap<String, String> product, HttpServletRequest request) {
        Integer grade = Integer.parseInt(product.get("grade"));
        String loginId = jwtTokenProvider.getUserPk(request.getHeader("X-AUTH-TOKEN"));
        User user = pointService.findUser(loginId);

        Long newPoint = pointService.increasePoint(user, grade);
        Rank rank = pointService.rankUpIfPointReach(user);
        pointService.discountUpIfRankUp(user, rank);
        ResponseDto responseDto = ResponseDto.builder()
                .status(200)
                .responseMessage("구매 성공")
                .data(new PurchaseDto(user.getPoint(), user.getRank(), user.getDiscount()))
                .build();
        return ResponseEntity.ok(responseDto);
    }


}
