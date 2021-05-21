package donot.gas.back.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
public class User implements UserDetails {
    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    private String username;
    private String loginId;
    private String password;
    private Long point;
    private String role;
    private Integer discount;

    @Enumerated(EnumType.STRING)
    private Rank rank;

    public User(String username, String loginId, String password, Long point, String role, Rank rank, Integer discount) {
        this.username = username;
        this.loginId = loginId;
        this.password = password;
        this.point = point;
        this.role = role;
        this.rank = rank;
        this.discount = discount;
    }

    @OneToMany(mappedBy = "user")
    private List<History> historyList = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + this.role));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
