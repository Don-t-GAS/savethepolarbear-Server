package donot.gas.back.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "orders")
public class Order {
    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    private String company;
    private String kinds;
    private String model;
    private Integer grade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    protected Order() {
    }

    public Order(String company, String kinds, String model, Integer grade) {
        this.company = company;
        this.kinds = kinds;
        this.model = model;
        this.grade = grade;
    }

    public void setUser(User user) {
        this.user = user;
        user.getOrderList().add(this);
    }
}
