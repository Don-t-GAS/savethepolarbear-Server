package donot.gas.back.repository.history;

import donot.gas.back.dto.CountDto;
import donot.gas.back.entity.History;
import donot.gas.back.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class HistoryJpaRepository {
    private final EntityManager em;

    public CountDto getCountByGrade(Long userId) {
        ArrayList<Long> cntList = new ArrayList<>();
        for(int i=1; i<=5; i++) {
            Long cnt = em.createQuery(
                    "select COUNT(h.id) from History h" +
                            " where h.user.id=:userId and h.grade=:grade", Long.class)
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
