package donot.gas.back.repository;

import donot.gas.back.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QuizRepository extends JpaRepository<Quiz, Long> {

    @Query(value = "SELECT quiz_id from dontgas.quiz order by rand() limit 1", nativeQuery = true)
    Long findRandomQuiz();
}
