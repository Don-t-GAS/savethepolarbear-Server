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
    @Enumerated(EnumType.STRING)
    private Rank rank;

    public User(String username, String loginId, String password, Long point, String role) {
        this.username = username;
        this.loginId = loginId;
        this.password = password;
        this.point = point;
        this.role = role;
    }

    @OneToMany(mappedBy = "user")
    private List<History> historyList = new ArrayList<>();

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Discount discount;

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
