package ru.practicum.item;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

import javax.persistence.SqlResultSetMapping;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemCountByUser {
    private Long userId;
    private Long count;
}