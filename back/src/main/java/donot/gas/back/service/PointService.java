package donot.gas.back.service;

import donot.gas.back.dto.PointDto;
import donot.gas.back.entity.Rank;
import donot.gas.back.entity.User;
import donot.gas.back.exception.user.NoExistUserException;
import donot.gas.back.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class PointService {
    private final UserRepository userRepository;

    @Autowired
    public PointService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findUser(String loginId) throws NoExistUserException {
        Optional<User> user = userRepository.findByLoginId(loginId);
        user.orElseThrow(NoExistUserException::new);
        return user.get();
    }

    public Long increasePoint(User user, Integer grade) {
        Long newPoint = user.getPoint() + PointDto.getPoint(grade);
        user.setPoint(newPoint);
        userRepository.save(user);
        return newPoint;
    }

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
