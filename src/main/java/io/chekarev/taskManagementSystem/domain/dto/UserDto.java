package io.chekarev.taskManagementSystem.domain.dto;

import io.chekarev.taskManagementSystem.models.Role;
import lombok.*;

/**
 * UserDto
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserDto {
    private long id;
    private String email;
    private String password;
    private String name;
    private Role role;

    public UserDto(String email, String password, String name, Role role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", username='" + name + '\'' +
                ", role=" + role +
                '}';
    }
}