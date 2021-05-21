package donot.gas.back.dto;

import donot.gas.back.entity.User;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
public class UserDto {
    private final Long id;
    private final String username;
    private final String role;

    public UserDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.role = user.getRole();
    }
}
