package io.chekarev.taskManagementSystem.domain.dto;

import lombok.*;

/**
 * UserDtoLogin
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserDtoLogin{
    private String email;
    private String password;

    @Override
    public String toString() {
        return "UserDtoRegister{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}