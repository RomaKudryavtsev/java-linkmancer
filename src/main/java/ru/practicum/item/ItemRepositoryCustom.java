package ru.practicum.item;

import java.util.List;

public interface ItemRepositoryCustom {
    List<ItemInfoWithUrlState> findUrlAndStatusByUserId(long userId);
}
