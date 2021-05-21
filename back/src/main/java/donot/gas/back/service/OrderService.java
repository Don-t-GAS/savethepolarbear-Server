package donot.gas.back.service;

import donot.gas.back.dto.PointDto;
import donot.gas.back.entity.Order;
import donot.gas.back.entity.Rank;
import donot.gas.back.entity.User;
import donot.gas.back.exception.user.NoExistUserException;
import donot.gas.back.repository.OrderRepository;
import donot.gas.back.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public void createOrder(String company, String kinds, String model, Integer grade, User user) {
        increasePoint(user, grade);
        Rank newRank = rankUpIfPointReach(user);
        discountUpIfRankUp(user, newRank);

        Order order = new Order(company, kinds, model, grade);

        List<Order> findOrderByModel = orderRepository.findOrderByModel(user.getId(), model);

        if (findOrderByModel.isEmpty()) {
            order.setOrderCount(1);
            orderRepository.save(order);
            order.setUser(user);
        } else {
            Order findOrder = findOrderByModel.get(0);
            findOrder.setOrderCount(findOrder.getOrderCount() + 1);
        }
    }

    public User findUser(String loginId) throws NoExistUserException {
        Optional<User> user = userRepository.findByLoginId(loginId);
        user.orElseThrow(NoExistUserException::new);
        return user.get();
    }

    @Transactional
    public void increasePoint(User user, Integer grade) {
        Long newPoint = user.getPoint() + PointDto.getPoint(grade);
        user.setPoint(newPoint);
        userRepository.save(user);
    }

    @Transactional
    public Rank rankUpIfPointReach(User user) {
        if(user.getRank() == Rank.BRONZE && user.getPoint() >= 50) {
            user.setRank(Rank.SILVER);
            userRepository.save(user);
            return Rank.SILVER;
        }
        else if(user.getRank() == Rank.SILVER && user.getPoint() >= 100) {
            user.setRank(Rank.GOLD);
            userRepository.save(user);
            return Rank.GOLD;
        }
        return null;
    }

    @Transactional
    public void discountUpIfRankUp(User user, Rank rank) {
        if(rank != null) {
            if(rank == Rank.SILVER) {
                user.setDiscount(8);
                userRepository.save(user);
            }
            else if(rank == Rank.GOLD) {
                user.setDiscount(10);
                userRepository.save(user);
            }
        }
    }
}
