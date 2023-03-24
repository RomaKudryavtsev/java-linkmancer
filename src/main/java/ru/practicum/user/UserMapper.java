package ru.practicum.user;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class UserMapper {
    public static UserDto mapToUserDto(User user) {
        String regDate = DateTimeFormatter
                .ofPattern("yyyy.MM.dd hh:mm:ss")
                .withZone(ZoneOffset.UTC)
                .format(user.getRegistrationDate());

        return new UserDto(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(), regDate, user.getState());
    }

    public static List<UserDto> mapToUserDto(Iterable<User> users) {
        List<UserDto> result = new ArrayList<>();

        for (User user : users) {
            result.add(mapToUserDto(user));
        }

        return result;
    }

    public static User mapToNewUser(UserDto userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setState(userDto.getState());
        return user;
    }
}