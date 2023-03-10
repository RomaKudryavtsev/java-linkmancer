package ru.practicum.user;

import java.util.List;

public interface UserRepository {
    List<User> findAll();
    User save(User user);
}
