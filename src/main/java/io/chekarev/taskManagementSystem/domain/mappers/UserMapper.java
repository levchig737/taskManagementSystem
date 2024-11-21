package io.chekarev.taskManagementSystem.domain.mappers;

import io.chekarev.taskManagementSystem.domain.dto.UserDto;
import io.chekarev.taskManagementSystem.domain.dto.UserDtoRegisterUpdate;
import io.chekarev.taskManagementSystem.domain.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    //// UserDto
    /**
     * User to UserDto
     * @param user userEntity
     * @return UserDto
     */
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "role", target = "role")
    UserDto userToUserDto(User user);

    /**
     * UserDto to User
     * @param userDto userDto
     * @return UserEntity
     */
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "role", target = "role")
    User userDtoToUser(UserDto userDto);

    //UserDtoRegister
    /**
     * User to UserDtoRegister
     * @param user userEntity
     * @return UserDtoRegister
     */
    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    UserDtoRegisterUpdate userToUserDtoRegisterUpdate(User user);

    /**
     * UserDtoRegister to User
     * @param userDtoRegisterUpdate userDtoRegister
     * @return UserEntity
     */
    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    User userDtoRegisterUpdateToUser(UserDtoRegisterUpdate userDtoRegisterUpdate);
}


