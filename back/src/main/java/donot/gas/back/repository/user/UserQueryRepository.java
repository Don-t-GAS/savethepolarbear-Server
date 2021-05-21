package donot.gas.back.repository.user;

import donot.gas.back.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserQueryRepository {

    private final EntityManager em;

    // loginId을 기준으로 유저 정보를 받아온다.
    public List<User> findByLoginId(String loginId) {
        return em.createQuery(
                "select distinct u from User u" +
                        " join fetch u.historyList h" +
                        " where u.loginId=:loginId", User.class)
                .setParameter("loginId", loginId)
                .getResultList();
    }
}
