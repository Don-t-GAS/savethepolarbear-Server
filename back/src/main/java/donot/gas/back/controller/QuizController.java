package donot.gas.back.controller;

import donot.gas.back.auth.JwtTokenProvider;
import donot.gas.back.dto.AnswerDto;
import donot.gas.back.dto.ResponseDto;
import donot.gas.back.entity.Quiz;
import donot.gas.back.entity.Rank;
import donot.gas.back.entity.User;
import donot.gas.back.repository.QuizRepository;
import donot.gas.back.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<?> answerQuiz(@RequestParam("id") Long id, @RequestParam("answer") Boolean answer, HttpServletRequest request) {
        String loginId = jwtTokenProvider.getUserPk(request.getHeader("X-AUTH-TOKEN"));
        User user = orderService.findUser(loginId);

        Quiz quiz = quizRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당하는 문제가 없습니다."));
        boolean realAnswer = quiz.isAnswer();

        if (realAnswer == answer) {
            orderService.increasePoint(user, 4);
            Rank newRank = orderService.rankUpIfPointReach(user);
            orderService.discountUpIfRankUp(user, newRank);

            AnswerDto answerDto = AnswerDto.builder()
                    .isAnswer(true)
                    .upPoint(2)
                    .build();

            ResponseDto responseDto = ResponseDto.builder()
                    .status(200)
                    .responseMessage("퀴즈 문제풀이 결과 출력[정답] 성공")
                    .data(answerDto)
                    .build();

            return ResponseEntity.ok(responseDto);
        } else {
            orderService.increasePoint(user, 5);
            Rank newRank = orderService.rankUpIfPointReach(user);
            orderService.discountUpIfRankUp(user, newRank);

            AnswerDto answerDto = AnswerDto.builder()
                    .isAnswer(false)
                    .upPoint(1)
                    .build();

            ResponseDto responseDto = ResponseDto.builder()
                    .status(200)
                    .responseMessage("퀴즈 문제풀이 결과 출력[오답] 성공")
                    .data(answerDto)
                    .build();

            return ResponseEntity.ok(responseDto);
        }
    }
}