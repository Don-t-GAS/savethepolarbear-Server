package donot.gas.back.controller;

import donot.gas.back.auth.JwtTokenProvider;
import donot.gas.back.dto.ErrorDto;
import donot.gas.back.dto.JwtDto;
import donot.gas.back.dto.ResponseDto;
import donot.gas.back.dto.UserDto;
import donot.gas.back.entity.User;
import donot.gas.back.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    // 회원가입
    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody Map<String, String> user) {
        try {
            if (userRepository.findByLoginId(user.get("loginId")).isPresent()) {
                throw new IllegalArgumentException();
            }
            User newUser = new User(user.get("username"), user.get("loginId"), passwordEncoder.encode(user.get("password")), 0L, "USER");
            userRepository.save(newUser);

            return ResponseEntity.ok(new UserDto(newUser));
        } catch (IllegalArgumentException e) {
            return (ResponseEntity<?>) ResponseEntity.badRequest();
        }

    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> user) throws Exception {
        User member = userRepository.findByLoginId(user.get("loginId"))
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));
        if (!passwordEncoder.matches(user.get("password"), member.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
        ResponseDto responseDto = ResponseDto.builder()
                .status(200)
                .responseMessage("로그인 성공")
                .data(new JwtDto(jwtTokenProvider.createToken(member.getLoginId(), member.getRole())))
                .build();
        return ResponseEntity.ok(responseDto);
    }

    // 모든 User 정보
    @GetMapping("/api/all")
    public ResponseEntity<?> findAll(HttpServletRequest request) {
        List<User> userList = userRepository.findAll();
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user : userList) {
            userDtoList.add(new UserDto(user));
        }

        System.out.println("사용자 : " + jwtTokenProvider.getUserPk(request.getHeader("X-AUTH-TOKEN")));
        ResponseDto responseDto = ResponseDto.builder()
                .status(200)
                .responseMessage("모든 회원 조회 성공")
                .data(userDtoList)
                .build();
        return ResponseEntity.ok(responseDto);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> badRequest(Exception e) {
        System.err.println(e.getClass());
        ErrorDto error = ErrorDto.builder()
                .status(400)
                .responseMessage("잘못된 입력이 들어왔습니다.")
                .build();
        return ResponseEntity.badRequest().body(error);
    }
}