package io.chekarev.taskManagementSystem.domain.dto;

import lombok.*;

/**
 * UserDtoRegisterUpdate
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserDtoRegisterUpdate {
    private String email;
    private String password;
    private String name;

    @Override
    public String toString() {
        return "UserDtoRegister{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}