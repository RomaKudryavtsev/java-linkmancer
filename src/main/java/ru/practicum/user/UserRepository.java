package ru.practicum.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(path = "people")
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

    @RestResource(path = "emails")
    List<UserShort> findAllByEmailContainingIgnoreCase(@Param("email") String email);
}