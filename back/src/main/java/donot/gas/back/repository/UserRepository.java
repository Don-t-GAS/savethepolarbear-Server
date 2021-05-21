package donot.gas.back.repository;

import donot.gas.back.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User save(User user);
    Optional<User> findByLoginId(String loginId);
    List<User> findAll();
}
