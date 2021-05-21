package donot.gas.back.repository.history;

import donot.gas.back.dto.CountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.ArrayList;

@Repository
@RequiredArgsConstructor
public class HistoryJpaRepository {
    private final EntityManager em;

    public CountDto getCountByGrade(Long userId) {
        ArrayList<Long> cntList = new ArrayList<>();
        for(int i=1; i<=5; i++) {
            Long cnt = em.createQuery(
                    "select COUNT(o.id) from Order o" +
                            " where o.user.id=:userId and o.grade=:grade", Long.class)
                    .setParameter("userId", userId)
                    .setParameter("grade", i)
                    .getSingleResult();
            System.out.println("THIS: " + cnt);
            cntList.add(cnt);
        }
        return new CountDto(
                cntList.get(0),
                cntList.get(1),
                cntList.get(2),
                cntList.get(3),
                cntList.get(4));
    }
}
