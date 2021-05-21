package donot.gas.back.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class History {
    @Id @GeneratedValue
    @Column(name = "history_id")
    private Long id;
    private String company;
    private String kinds;
    private String model;
    private Integer grade;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void setUser(User user) {
        this.user = user;
        user.getHistoryList().add(this);
    }
}
