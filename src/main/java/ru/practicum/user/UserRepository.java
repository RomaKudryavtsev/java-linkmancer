package ru.practicum.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    List<UserShort> findAllByEmailContainingIgnoreCase(String email);
}