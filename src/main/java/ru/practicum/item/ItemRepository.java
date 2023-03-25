package ru.practicum.item;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {
    List<Item> findByUserId(long userId);
    List<ItemInfo> findUrlByUserId(long userId);
    void deleteByUserIdAndId(long userId, long id);
}