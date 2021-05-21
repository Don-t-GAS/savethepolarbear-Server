package donot.gas.back.entity;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Discount {
    @Id @GeneratedValue
    @Column(name = "discount_id")
    private Long id;
    private Integer percent;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    public void setUser(User user) {
        this.user = user;
        user.setDiscount(this);
    }
}
