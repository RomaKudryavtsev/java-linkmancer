package ru.practicum.user;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

interface UserService {
    List<UserDto> getAllUsers();

    List<UserShort> getUsersByEmail(String email);

    List<UserShortWithIP> getUsersByEmailWithIp(String email);

    UserDto saveUser(UserDto userDto);
}