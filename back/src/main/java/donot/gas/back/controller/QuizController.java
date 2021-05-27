package donot.gas.back.controller;

import donot.gas.back.auth.JwtTokenProvider;
import donot.gas.back.dto.ResponseDto;
import donot.gas.back.entity.Quiz;
import donot.gas.back.entity.Rank;
import donot.gas.back.entity.User;
import donot.gas.back.repository.QuizRepository;
import donot.gas.back.repository.user.UserRepository;
import donot.gas.back.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class QuizController {

    private final JwtTokenProvider jwtTokenProvider;
    private final QuizRepository quizRepository;
    private final OrderService orderService;

    @GetMapping("/api/quiz")
    public ResponseEntity<?> selectQuiz() {

        Long randomQuizId = quizRepository.findRandomQuiz();
        Quiz quiz = quizRepository.findById(randomQuizId).orElseThrow(() -> new IllegalArgumentException("문제가 없습니다."));

        ResponseDto responseDto = ResponseDto.builder()
                .status(200)
                .responseMessage("퀴즈 출제 성공")
                .data(quiz)
                .build();
        return ResponseEntity.ok(responseDto);
    }

    @RequestMapping("/api/quiz/check")
    public boolean answerQuiz(@RequestParam("id") Long id, @RequestParam("answer") Boolean answer, HttpServletRequest request) {

        String loginId = jwtTokenProvider.getUserPk(request.getHeader("X-AUTH-TOKEN"));
        User user = orderService.findUser(loginId);

        Quiz quiz = quizRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당하는 문제가 없습니다."));
        boolean realAnswer = quiz.isAnswer();

        if (realAnswer == answer) {
            orderService.increasePoint(user, 4);
            Rank newRank = orderService.rankUpIfPointReach(user);
            orderService.discountUpIfRankUp(user, newRank);

            return true;
        } else {
            orderService.increasePoint(user, 5);
            Rank newRank = orderService.rankUpIfPointReach(user);
            orderService.discountUpIfRankUp(user, newRank);

            return false;
        }
    }
}
