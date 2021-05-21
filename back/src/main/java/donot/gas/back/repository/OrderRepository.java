package donot.gas.back.repository;

import donot.gas.back.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
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
}
