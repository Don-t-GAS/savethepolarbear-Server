package donot.gas.back.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class User {
    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    private String username;
    private String loginId;
    private String password;
    private Long point;
    private String role;
    @Enumerated(EnumType.STRING)
    private Rank rank;

    @OneToMany(mappedBy = "user")
    private List<History> historyList = new ArrayList<>();

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Discount discount;
}
