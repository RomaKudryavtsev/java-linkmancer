package ru.practicum.user;

import java.util.List;

interface UserService {
    List<UserDto> getAllUsers();
    UserDto saveUser(UserDto userDto);
}