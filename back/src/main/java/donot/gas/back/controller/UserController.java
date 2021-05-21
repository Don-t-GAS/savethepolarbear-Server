package donot.gas.back.controller;

import donot.gas.back.auth.JwtTokenProvider;
import donot.gas.back.dto.ErrorDto;
import donot.gas.back.dto.JwtDto;
import donot.gas.back.dto.ResponseDto;
import donot.gas.back.dto.UserDto;
import donot.gas.back.entity.Rank;
import donot.gas.back.entity.User;
import donot.gas.back.exception.user.*;
import donot.gas.back.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Autowired
    public UserController(PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    // 회원가입
    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody Map<String, String> user) {
        if (user.get("loginId") == null || user.get("password") == null || user.get("username") == null) {
            throw new NoArgException();
        }
        if (user.get("username").isEmpty() || user.get("username").isBlank()) {
            throw new UsernameValidationException();
        }
        if (8 > user.get("loginId").length() || user.get("loginId").isEmpty() || user.get("loginId").isBlank() || user.get("loginId").contains(" ")) {
            throw new LoginIdValidationException();
        }
        if (8 > user.get("password").length() || user.get("password").isEmpty() || user.get("password").isBlank() || user.get("password").contains(" ")) {
            throw new PasswordValidationException();
        }
        if (userRepository.findByLoginId(user.get("loginId")).isPresent()) {
            throw new ExistLoginIdException();
        }
        User newUser = new User(
                user.get("username"),
                user.get("loginId"),
                passwordEncoder.encode(user.get("password")),
                0L,
                "USER",
                Rank.BRONZE,
                5);
        userRepository.save(newUser);
        ResponseDto responseDto = ResponseDto.builder()
                .status(200)
                .responseMessage("회원가입 성공")
                .data(new UserDto(newUser))
                .build();
        return ResponseEntity.ok(responseDto);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> user) {
        if (user.get("loginId") == null || user.get("password") == null) {
            throw new NoArgException();
        }
        User member = userRepository.findByLoginId(user.get("loginId"))
                .orElseThrow(NoExistUserException::new);

        if (!passwordEncoder.matches(user.get("password"), member.getPassword())) {
            throw new MismatchPasswordException();
        }
        ResponseDto responseDto = ResponseDto.builder()
                .status(200)
                .responseMessage("로그인 성공")
                .data(new JwtDto(jwtTokenProvider.createToken(member.getLoginId(), member.getRole())))
                .build();
        return ResponseEntity.ok(responseDto);
    }

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

    @ExceptionHandler({
            ExistLoginIdException.class,
            NoExistUserException.class,
            MismatchPasswordException.class,
            LoginIdValidationException.class,
            PasswordValidationException.class,
            NoArgException.class,
            UsernameValidationException.class
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