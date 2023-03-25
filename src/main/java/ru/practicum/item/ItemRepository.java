package ru.practicum.item;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByUserId(long userId);

    void deleteByUserIdAndId(long userId, long id);
}