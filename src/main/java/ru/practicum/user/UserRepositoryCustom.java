package ru.practicum.user;

import java.util.List;

public interface UserRepositoryCustom {
    List<UserShortWithIP> findAllByEmailContainingIgnoreCaseWithIP(String emailSearch);
}