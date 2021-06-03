package donot.gas.back.repository;

import donot.gas.back.dto.CountDto;
import donot.gas.back.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public List<Order> findOrderByModel(Long userId, String model) {
        return em.createQuery("select o from Order o where o.model=:model and o.user.id=:userId", Order.class)
                .setParameter("model", model)
                .setParameter("userId", userId)
                .getResultList();
    }

    public CountDto getCountByGrade(Long userId) {
        ArrayList<Integer> cntList = new ArrayList<>();
        for(int i=1; i<=5; i++) {
            List<Order> orderList = em.createQuery(
                    "select o from Order o" +
                            " where o.user.id=:userId and o.grade=:grade", Order.class)
                    .setParameter("userId", userId)
                    .setParameter("grade", i)
                    .getResultList();
            Integer total = 0;
            for(Order order : orderList) {
                total += order.getOrderCount();
            }
            cntList.add(total);
        }
        return new CountDto(
                cntList.get(0),
                cntList.get(1),
                cntList.get(2),
                cntList.get(3),
                cntList.get(4));
    }
}