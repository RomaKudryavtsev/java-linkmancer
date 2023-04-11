package ru.practicum.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserShortWithIP implements UserShort {
    String firstName;
    String email;
    String ip;

    public UserShortWithIP(UserShort user, String ip) {
        this.firstName = user.getFirstName();
        this.email = user.getEmail();
        this.ip = ip;
    }
}