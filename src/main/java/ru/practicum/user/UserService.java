package ru.practicum.user;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();

    List<UserShort> getUsersByEmail(String email);

    List<UserShortWithIP> getUsersByEmailWithIp(String email);

    UserDto saveUser(UserDto userDto);
}